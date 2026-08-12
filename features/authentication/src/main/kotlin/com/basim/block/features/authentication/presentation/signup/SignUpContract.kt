package com.basim.block.features.authentication.presentation.signup

import com.basim.block.features.authentication.domain.validation.PasswordValidationResult

data class SignUpUiState(
    val email: String = "",
    val emailInvalidCount: Int = 0,
    val password: String = "",
    val passwordInvalidCount: Int = 0,
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val passwordValidation: PasswordValidationResult = PasswordValidationResult(emptySet()),
)

/** Things the user does on the screen (screen → ViewModel). */
sealed interface SignUpUiAction {
    data class EmailChanged(val value: String) : SignUpUiAction
    data class PasswordChanged(val value: String) : SignUpUiAction
    data class TermsAcceptedChanged(val value: Boolean) : SignUpUiAction
    data object SignUpClicked : SignUpUiAction
}

/**
 * One-shot notifications the ViewModel sends to the screen (ViewModel → screen).
 * Delivered via Channel — fired once, never replayed.
 */
sealed interface SignUpUiEvent {
    /** Account creation succeeded — the screen should navigate onward. */
    data object SignUpSucceeded : SignUpUiEvent
}
