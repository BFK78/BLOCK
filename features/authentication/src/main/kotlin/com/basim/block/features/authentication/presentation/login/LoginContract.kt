package com.basim.block.features.authentication.presentation.login

/** Everything the Login screen renders. Immutable single source of truth. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
)

/** Things the user does on the screen (screen → ViewModel). */
sealed interface LoginUiAction {
    data class EmailChanged(val value: String) : LoginUiAction
    data class PasswordChanged(val value: String) : LoginUiAction
}

/**
 * One-shot notifications the ViewModel sends to the screen (ViewModel → screen).
 * Delivered via Channel — fired once, never replayed. Empty until sign-in logic lands,
 * e.g. `data object SignInSucceeded : LoginUiEvent`.
 */
sealed interface LoginUiEvent
