package com.basim.block.features.authentication.presentation.login

import com.basim.block.core.common.error.AppError

/** Everything the Login screen renders. Immutable single source of truth. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val passwordErrorMessage: String = "",
    /** Last sign-in failure, as a typed error the UI can map to a message. Cleared on edit/retry. */
    val error: AppError? = null,
)

/** Things the user does on the screen (screen → ViewModel). */
sealed interface LoginUiAction {
    data class EmailChanged(val value: String) : LoginUiAction
    data class PasswordChanged(val value: String) : LoginUiAction
    data object SignInClicked : LoginUiAction
}

/**
 * One-shot notifications the ViewModel sends to the screen (ViewModel → screen).
 * Delivered via Channel — fired once, never replayed.
 */
sealed interface LoginUiEvent {
    /** Authentication succeeded — the screen should navigate onward. */
    data object SignInSucceeded : LoginUiEvent
}
