# Layer responsibilities

How the `data`, `domain`, and `presentation` packages divide work inside a single feature module.
The dependency direction inside a feature is always **presentation → domain ← data**: both
presentation and data depend on domain; domain depends on neither.

## Table of contents

1. [domain (pure Kotlin)](#domain-pure-kotlin)
2. [data](#data)
3. [presentation](#presentation)
4. [Hilt wiring](#hilt-wiring)
5. [Room — current reality & how to add it](#room--current-reality--how-to-add-it)
6. [Shared code → core modules](#shared-code--core-modules)

---

## domain (pure Kotlin)

The heart of the feature. Keep it free of Android, Compose, Hilt, and Room imports — only Kotlin and
`kotlinx.coroutines` (`Flow`/`suspend`). It's "pure Kotlin by convention": this is a single Android
module, so there's no compiler wall stopping an Android import — keep it clean in review. The payoff
is fast, dependency-free JUnit tests and a stable contract that the UI and data sides both build on.

### `domain/model/`

Plain immutable data classes the rest of the app speaks in. No Room `@Entity`, no DTOs, no
Android types.

```kotlin
package com.basim.block.features.authentication.domain.model

data class User(
    val id: String,
    val email: String,
    val displayName: String,
)
```

### `domain/repository/`

**Interfaces only.** The domain declares what it needs; `data` provides the implementation. This is
what lets domain stay pure while the real work (Room, network) lives in `data`.

```kotlin
package com.basim.block.features.authentication.domain.repository

import com.basim.block.features.authentication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeCurrentUser(): Flow<User?>
    suspend fun login(email: String, password: String): Result<User>
}
```

### `domain/usecase/`

One class per use-case, named for the action, invoked with `operator fun invoke`. A use-case holds a
single piece of business logic and depends only on repository interfaces (and other use-cases). Keep
them thin — if a use-case is just a pass-through, it's still fine; it keeps the ViewModel decoupled
from repositories.

```kotlin
package com.basim.block.features.authentication.domain.usecase

import com.basim.block.features.authentication.domain.model.User
import com.basim.block.features.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        repository.login(email.trim(), password)
}
```

> `@Inject` on a constructor is the one Hilt annotation that's fine in domain — it's just a
> constructor marker (`javax.inject`), not an Android dependency, and it lets Hilt build use-cases
> with zero extra module code.

---

## data

Implements the domain's repository interfaces and owns all the messy details: persistence, mapping,
and (if the feature has one) remote access.

### `data/local/`

Room `@Entity` classes and `@Dao` interfaces. **DAOs return `Flow`** for reads so the UI updates
reactively; writes are `suspend`.

```kotlin
package com.basim.block.features.authentication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val displayName: String,
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Upsert
    suspend fun upsert(user: UserEntity)
}
```

### `data/remote/` (optional)

Only if the feature talks to an API. The project hasn't picked a networking stack yet — if you need
one, **ask first** rather than introducing Retrofit/Ktor unilaterally. This package is the slot for
DTOs and the API service when that decision is made.

### `data/mapper/`

Pure functions translating between layers: `Entity`/`DTO` ↔ domain `model`. Keep mapping out of DAOs
and repositories so it's testable and obvious.

```kotlin
package com.basim.block.features.authentication.data.mapper

import com.basim.block.features.authentication.data.local.UserEntity
import com.basim.block.features.authentication.domain.model.User

fun UserEntity.toDomain() = User(id = id, email = email, displayName = displayName)
fun User.toEntity() = UserEntity(id = id, email = email, displayName = displayName)
```

### `data/repository/`

`XxxRepositoryImpl` implementing the domain interface. Combines data sources, applies mappers,
chooses dispatchers. Returns domain models — never leaks `Entity`/`DTO` upward.

```kotlin
package com.basim.block.features.authentication.data.repository

import com.basim.block.features.authentication.data.local.UserDao
import com.basim.block.features.authentication.data.mapper.toDomain
import com.basim.block.features.authentication.domain.model.User
import com.basim.block.features.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : AuthRepository {

    override fun observeCurrentUser(): Flow<User?> =
        userDao.observeCurrentUser().map { it?.toDomain() }

    override suspend fun login(email: String, password: String): Result<User> {
        // ...authenticate, persist via userDao, return mapped domain model
        TODO("implement against the real data source")
    }
}
```

### `data/di/`

See [Hilt wiring](#hilt-wiring).

---

## presentation

Per-screen MVI. The UI's only jobs are: render `UiState`, emit `UiEvent`, and consume `UiEffect`. No
business logic, no repository/DAO access, no mapping. Full code in `mvi-contract.md`. Each screen
gets
its own package (`presentation/login/`, `presentation/registration/`, …); composables shared
*within*
the feature go in `common/components/`, and anything reused *across* features goes down into
`:core:designkit`.

---

## Hilt wiring

DI is Hilt with **KSP** (not kapt) — both already applied to every feature module by
`block.android.feature`. Per feature, add one or more `@Module`s under `data/di/`:

- **`@Binds`** an abstract function to bind `RepositoryImpl` → repository interface.
- **`@Provides`** for things you don't own the constructor of — DAOs, the Room database.

```kotlin
package com.basim.block.features.authentication.data.di

import com.basim.block.features.authentication.data.repository.AuthRepositoryImpl
import com.basim.block.features.authentication.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
```

Use-cases and `@HiltViewModel` ViewModels are constructor-injected, so they need no module entry.
DAOs are `@Provides`d from whichever module owns the Room database (see below).

> Prerequisite: feature `@HiltViewModel`s only resolve once the **app** is Hilt-enabled
> (`@HiltAndroidApp` Application + `@AndroidEntryPoint` activity). That isn't done yet — see
> `new-feature-checklist.md`.

---

## Room — current reality & how to add it

Room is the chosen database, but it is **not fully wired in the catalog yet**:

- `gradle/libs.versions.toml` has `room = "2.8.4"`, the `room-gradlePlugin` (build-logic only), and
  a
  `[plugins] room` entry — **but no** `androidx-room-runtime`, `androidx-room-ktx`, or
  `androidx-room-compiler` library aliases, and **no** Room convention plugin.
- The feature module already applies **KSP** (via Hilt), so the Room compiler can run there without
  extra plugin setup.

**To use Room, first add the library aliases** to `gradle/libs.versions.toml`:

```toml
[libraries]
androidx-room-runtime  = { group = "androidx.room", name = "room-runtime",  version.ref = "room" }
androidx-room-ktx      = { group = "androidx.room", name = "room-ktx",      version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
```

Then in the module that owns the database:

```kotlin
dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
```

**Where does the database live?** The project brief says core holds the *shared* database. The clean
split:

- A **`:core:database`** module owns the `@Database` class and provides the `RoomDatabase` instance
  via a Hilt module (`@Provides @Singleton`). Features depend on it (`:features:* -> :core:*` is
  allowed). This is the **recommended** home for the DB and is **not yet created** — propose it
  before
  building it.
- Each feature still defines its own `@Entity`/`@Dao` in `data/local/` and contributes them to the
  shared database, `@Provides`ing its DAO from the feature's `data/di` module.

If a feature's data is entirely private and you don't want a shared DB yet, a feature-local Room
database is acceptable as an interim step — but call that out so it can be reconciled with the
`:core:database` plan.

---

## Shared code → core modules

Anything two features would both want belongs in a `:core:*` module, never copied between features
and
never reached for sideways (`:features:* -X> :features:*` fails the build). Existing/likely cores:

- **`:core:designkit`** (exists) — theme (`BlockTheme`), Compose components, icons. Re-exposes
  Compose
  deps with `api(...)` so consumers inherit them.
- **`:core:database`** (recommended, not yet created) — shared Room database.
- **`:core:common`** / **`:core:util`** (create when a util is needed by 2+ features) — dispatchers,
  result wrappers, extension functions.
