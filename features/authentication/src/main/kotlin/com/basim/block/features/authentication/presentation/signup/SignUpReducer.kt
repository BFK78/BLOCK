package com.basim.block.features.authentication.presentation.signup

import com.basim.block.core.common.error.AppError
import com.basim.block.features.authentication.domain.validation.PasswordValidationResult

sealed interface SignUpChange {
    data class EmailUpdated(val value: String, val isValid: Boolean) : SignUpChange
    data class PasswordUpdated(
        val value: String,
        val error: PasswordValidationResult
    ) : SignUpChange

    data class TermsAcceptedUpdated(val value: Boolean) : SignUpChange
    data object Submitting : SignUpChange
    data object Succeeded : SignUpChange
    data object InvalidEmail : SignUpChange
    data object InvalidPassword : SignUpChange
    data class Failed(val error: AppError) : SignUpChange
}

object SignUpReducer {
    fun reduce(state: SignUpUiState, change: SignUpChange): SignUpUiState = when (change) {
        is SignUpChange.EmailUpdated -> state.copy(
            email = change.value,
            emailInvalidCount = if (change.isValid) 0 else state.emailInvalidCount,
        )

        is SignUpChange.PasswordUpdated -> state.copy(
            password = change.value,
            passwordValidation = change.error,
            passwordInvalidCount = if (change.error.isValid) 0 else state.passwordInvalidCount,
        )

        is SignUpChange.TermsAcceptedUpdated -> state.copy(termsAccepted = change.value)
        SignUpChange.Submitting -> state.copy(isLoading = true)
        SignUpChange.Succeeded -> state.copy(isLoading = false)
        is SignUpChange.Failed -> state.copy(isLoading = false)
        SignUpChange.InvalidEmail -> state.copy(emailInvalidCount = state.emailInvalidCount + 1)
        SignUpChange.InvalidPassword ->
            state.copy(passwordInvalidCount = state.passwordInvalidCount + 1)
    }
}
