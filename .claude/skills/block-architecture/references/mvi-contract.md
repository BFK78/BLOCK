# MVI contract

The exact shape of a presentation screen. Every screen package
(`presentation/<screen>/`) has four files: **Contract**, **Reducer**, **ViewModel**, **Screen**.
Code here targets this project's real setup — `src/main/kotlin`, the version-catalog accessors
already injected by `block.android.feature`, and a `Channel` for one-shot effects.

The running example is `presentation/login/` in `:features:authentication`.

> Directional naming: an **Action** is something the *user* does (screen → ViewModel); an **Event**
> is a one-shot notification the *ViewModel* sends back to the screen (ViewModel → screen).

## Table of contents

1. [Contract — State, Action, Event](#1-contract)
2. [Reducer — pure Kotlin](#2-reducer)
3. [ViewModel — @HiltViewModel](#3-viewmodel)
4. [Screen — Composable](#4-screen)
5. [Why Channel and not SharedFlow](#5-why-channel-and-not-sharedflow)
6. [Testing the reducer](#6-testing-the-reducer)

---

## 1. Contract

One file, `LoginContract.kt`, holding the three sealed/data types for the screen. Keeping them
together makes the screen's full surface readable at a glance.

```kotlin
package com.basim.block.features.authentication.presentation.login

import com.basim.block.features.authentication.domain.model.User

/** Everything the screen renders. Immutable; the single source of truth for the UI. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val emailError: String? = null,
    val user: User? = null,
)

/** Everything the user can do on the screen (screen → ViewModel). */
sealed interface LoginUiAction {
    data class EmailChanged(val value: String) : LoginUiAction
    data class PasswordChanged(val value: String) : LoginUiAction
    data object SubmitClicked : LoginUiAction
    data object RegisterClicked : LoginUiAction
}

/** One-shot notifications the ViewModel sends to the screen (ViewModel → screen).
 *  Delivered via Channel — fired once, never replayed. */
sealed interface LoginUiEvent {
    data object NavigateToHome : LoginUiEvent
    data object NavigateToRegister : LoginUiEvent
    data class ShowError(val message: String) : LoginUiEvent
}
```

`data object` (not `object`) is the idiom for no-payload variants — it gives sensible
`toString`/`equals`.

---

## 2. Reducer

`LoginReducer.kt` — **pure Kotlin**. The one place `LoginUiState` is recomputed. No coroutines, no
Android, no ViewModel reference. It takes the current state and a *partial change* and returns the
new
state, so it's trivially unit-testable and free of timing concerns.

Model the partial changes as their own sealed type — these are *results* of work (validation,
use-case outcomes), distinct from raw `UiAction`s. The ViewModel decides which change to apply; the
reducer just applies it.

```kotlin
package com.basim.block.features.authentication.presentation.login

import com.basim.block.features.authentication.domain.model.User

/** Internal, reduced facts about what happened — produced by the ViewModel, applied by the reducer. */
sealed interface LoginChange {
    data class EmailUpdated(val value: String) : LoginChange
    data class PasswordUpdated(val value: String) : LoginChange
    data object SubmitStarted : LoginChange
    data class LoginSucceeded(val user: User) : LoginChange
    data class LoginFailed(val message: String) : LoginChange
}

object LoginReducer {
    fun reduce(state: LoginUiState, change: LoginChange): LoginUiState = when (change) {
        is LoginChange.EmailUpdated ->
            state.copy(email = change.value, emailError = null)

        is LoginChange.PasswordUpdated ->
            state.copy(password = change.value)

        LoginChange.SubmitStarted ->
            state.copy(isSubmitting = true, emailError = null)

        is LoginChange.LoginSucceeded ->
            state.copy(isSubmitting = false, user = change.user)

        is LoginChange.LoginFailed ->
            state.copy(isSubmitting = false, emailError = change.message)
    }
}
```

> Side effects (navigation, snackbars) are **not** reducer output — the reducer only computes state.
> The ViewModel emits events on the `Channel` separately. That separation is what keeps the reducer
> pure.

---

## 3. ViewModel

`LoginViewModel.kt` — `@HiltViewModel`, constructor-injected use-cases. Owns the `MutableStateFlow`
for state and a `Channel` for events. Translates each `UiAction` into work + reducer changes,
running
suspending work in `viewModelScope`.

```kotlin
package com.basim.block.features.authentication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basim.block.features.authentication.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // BUFFERED so an event emitted before the screen collects isn't dropped.
    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginUiAction) {
        when (action) {
            is LoginUiAction.EmailChanged -> apply(LoginChange.EmailUpdated(action.value))
            is LoginUiAction.PasswordChanged -> apply(LoginChange.PasswordUpdated(action.value))
            LoginUiAction.SubmitClicked -> submit()
            LoginUiAction.RegisterClicked -> emit(LoginUiEvent.NavigateToRegister)
        }
    }

    private fun submit() {
        apply(LoginChange.SubmitStarted)
        val current = _state.value
        viewModelScope.launch {
            login(current.email, current.password)
                .onSuccess { user ->
                    apply(LoginChange.LoginSucceeded(user))
                    emit(LoginUiEvent.NavigateToHome)
                }
                .onFailure { error ->
                    val message = error.message ?: "Login failed"
                    apply(LoginChange.LoginFailed(message))
                    emit(LoginUiEvent.ShowError(message))
                }
        }
    }

    /** Single funnel for state changes — always through the pure reducer. */
    private fun apply(change: LoginChange) =
        _state.update { LoginReducer.reduce(it, change) }

    private fun emit(event: LoginUiEvent) {
        viewModelScope.launch { _events.send(event) }
    }
}
```

Conventions:

- State is read and written **only** through `_state` + the reducer (`apply`). Never mutate fields
  directly.
- Events always go through `emit` → the `Channel`.
- Suspending work runs in `viewModelScope`; the scope is cancelled automatically when the ViewModel
  clears.

---

## 4. Screen

`LoginScreen.kt` — stateless composable that observes state lifecycle-aware, collects events once,
and reports user actions back via `onAction`. The ViewModel is obtained with `hiltViewModel()`.

```kotlin
package com.basim.block.features.authentication.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    onNavigateHome: () -> Unit,
    onNavigateRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect one-shot events exactly once for this composition.
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                LoginUiEvent.NavigateToHome -> onNavigateHome()
                LoginUiEvent.NavigateToRegister -> onNavigateRegister()
                is LoginUiEvent.ShowError -> { /* show snackbar with event.message */
                }
            }
        }
    }

    LoginScreen(state = state, onAction = viewModel::onAction)
}

@Composable
private fun LoginScreen(
    state: LoginUiState,
    onAction: (LoginUiAction) -> Unit,
) {
    // Render state; call onAction(...) on user actions. Pure UI, no logic.
}
```

The split — `LoginRoute` (wires the ViewModel/navigation) vs `LoginScreen` (pure stateless UI) —
keeps the UI previewable and testable without Hilt. `collectAsStateWithLifecycle` and
`hiltViewModel` come from `lifecycle-runtime-compose` and `hilt-navigation-compose`, both already on
the feature module's classpath.

---

## 5. Why Channel and not SharedFlow

Events are **one-shot, must-deliver-once** notifications: navigate, show a snackbar. Requirements:

- **No replay.** A new collector (after a config change/recomposition) must not re-receive an old
  navigation event. A `Channel` doesn't replay; a `StateFlow`/`replay>0 SharedFlow` would re-fire
  and
  cause double navigation.
- **Buffered, not dropped.** `Channel(Channel.BUFFERED)` holds an event emitted before the screen
  starts collecting, so nothing is lost during the gap.
- **Exactly-once consumption.** Each event is received by a single collector and then gone.

State is the opposite — it must be retained and replayed to whoever observes — so state uses
`StateFlow` and events use `Channel`. This is a deliberate project rule, not a stylistic choice.

---

## 6. Testing the reducer

Because the reducer is pure, it's a plain JUnit test under `src/test` — no Robolectric, no
coroutines, no Hilt:

```kotlin
class LoginReducerTest {
    @org.junit.Test
    fun `submit started sets loading and clears error`() {
        val start = LoginUiState(emailError = "bad", isSubmitting = false)
        val result = LoginReducer.reduce(start, LoginChange.SubmitStarted)
        org.junit.Assert.assertTrue(result.isSubmitting)
        org.junit.Assert.assertNull(result.emailError)
    }
}
```

Run it with `./gradlew :features:authentication:testDebugUnitTest`. Test use-cases the same way by
faking the repository interface.
