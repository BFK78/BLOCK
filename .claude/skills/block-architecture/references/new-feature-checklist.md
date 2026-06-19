# New feature checklist

Ordered steps to stand up a brand-new feature module the Block way. Replace `<name>` with the
feature name (lowercase, single word where possible), e.g. `profile`. Module path
`:features:<name>`, namespace `com.basim.block.features.<name>`.

## 0. Prerequisites — fix these once for the whole project

These aren't wired yet. The first feature that needs Hilt ViewModels or Room must address them (or
you'll get a clean compile but no working DI / no database). Confirm with the user before editing
build/config files — that's project plumbing, separate from authoring a feature.

### a. Enable Hilt in `:app` (required for any `@HiltViewModel`)

Currently `:app` applies raw plugins and has no Hilt, no `@HiltAndroidApp` Application, and
`MainActivity` is a plain `ComponentActivity`. To make feature ViewModels resolvable:

- Apply Hilt to `:app` (add `block.android.hilt` — or the raw `hilt` + `ksp` plugins, matching how
  `:app` currently uses raw plugins rather than convention ones).
- Add an `Application` annotated `@HiltAndroidApp` and register it in the manifest.
- Annotate `MainActivity` with `@AndroidEntryPoint`.
- Add `implementation(project(":features:<name>"))` to `app/build.gradle.kts` (the app doesn't
  depend
  on any feature yet).

### b. Add a coroutines library alias (if you call coroutine APIs directly)

The catalog only sets an experimental compiler opt-in; there's no `kotlinx-coroutines-*` dependency.
ViewModel/Flow basics arrive transitively via lifecycle, but to use `kotlinx.coroutines` explicitly
add to `gradle/libs.versions.toml`:

```toml
[versions]
kotlinxCoroutines = "1.10.2"   # pick the version compatible with kotlin 2.3.x
[libraries]
kotlinx-coroutines-core    = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core",    version.ref = "kotlinxCoroutines" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinxCoroutines" }
```

### c. Add Room library aliases (if the feature persists data)

See `layer-responsibilities.md` → "Room — current reality". Add
`androidx-room-runtime/-ktx/-compiler`
aliases; the feature already has KSP via Hilt. Decide DB home (`:core:database` recommended).

### d. Navigation

Only `hilt-navigation-compose` exists in the catalog. If you adopt standard Compose Navigation, add
an
`androidx-navigation-compose` alias first.

---

## 1. Register the module

In `settings.gradle.kts`, add (the parent `:features` is already declared):

```kotlin
include(":features:<name>")
```

The colon path maps to the folder path: `:features:<name>` → `features/<name>/`.

## 2. Create `features/<name>/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.block.android.feature)
    alias(libs.plugins.block.android.library.compose)
}

android {
    namespace = "com.basim.block.features.<name>"
}

dependencies {
    implementation(project(":core:designkit"))
    // implementation(project(":core:database"))  // if the feature uses the shared DB
}
```

Don't manually add Hilt, KSP, lifecycle-viewmodel-compose, or hilt-navigation-compose — the feature
plugin supplies them.

## 3. Create the source skeleton

Use `src/main/kotlin/` (feature & core modules use `kotlin/`, not `java/`):

```
features/<name>/src/main/
├── AndroidManifest.xml          # <manifest></manifest>, no package attribute
├── res/values/strings.xml       # string ids prefixed features_<name>_
└── kotlin/com/basim/block/features/<name>/
    ├── data/{local,mapper,repository,di}/
    ├── domain/{model,repository,usecase}/
    └── presentation/<screen>/
```

Resource prefix is auto-enforced as `features_<name>_` — every string/resource id must start with it
or lint fails.

## 4. Fill in the layers

Per the other references:

- **domain** → models, repository interfaces, use-cases (`layer-responsibilities.md`).
- **data** → Room entities/DAOs, mappers, `RepositoryImpl`, Hilt `@Module` in `data/di/`
  (`layer-responsibilities.md`).
- **presentation** → one package per screen with Contract + Reducer + ViewModel + Screen
  (`mvi-contract.md`).

## 5. Wire navigation into the app

Expose an entry composable (e.g. `LoginRoute`) and add it to the app's navigation. Remember the app
must `implementation(project(":features:<name>"))` and be Hilt-enabled (step 0a).

## 6. Tests

Add pure-Kotlin JUnit tests under `features/<name>/src/test/` for the reducer and use-cases (no
Android needed). Run:

```bash
./gradlew :features:<name>:testDebugUnitTest
```

## 7. Build to verify the module graph

```bash
./gradlew :features:<name>:assembleDebug
```

`moduleGraphAssert` runs on build — if you accidentally added a `:features:* -> :features:*` or
`:core:* -> :features:*` edge, the build fails here. Keep shared code in `:core:*`.

---

## Quick self-check before you call it done

- [ ] Module is one feature; layers are `data`/`domain`/`presentation` **packages**, not modules.
- [ ] `domain` has no Android / Compose / Room imports.
- [ ] Repository **interface** in domain; **Impl** in data; bound via Hilt `@Binds`.
- [ ] UiState is `StateFlow`; events go through a `Channel`; new state only via the pure reducer.
- [ ] No `:features:* -> :features:*` dependency; shared code lives in `:core:*`.
- [ ] Resources prefixed `features_<name>_`; sources under `src/main/kotlin`.
- [ ] If Hilt ViewModels are used: app is Hilt-enabled and depends on the feature.
