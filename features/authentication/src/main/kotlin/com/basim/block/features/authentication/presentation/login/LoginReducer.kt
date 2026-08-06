package com.basim.block.features.authentication.presentation.login

import com.basim.block.core.common.error.AppError

/** Reduced facts produced by the ViewModel, applied by the reducer. */
sealed interface LoginChange {
    data class EmailUpdated(val value: String) : LoginChange
    data class PasswordUpdated(val value: String) : LoginChange
    data object Submitting : LoginChange
    data object Succeeded : LoginChange
    data class Failed(val error: AppError) : LoginChange
}

object LoginReducer {
    fun reduce(state: LoginUiState, change: LoginChange): LoginUiState = when (change) {
        // Editing a field also clears a stale error so the message doesn't linger past a correction.
        is LoginChange.EmailUpdated -> state.copy(email = change.value, error = null)
        is LoginChange.PasswordUpdated -> state.copy(password = change.value, error = null)
        LoginChange.Submitting -> state.copy(isLoading = true, error = null)
        LoginChange.Succeeded -> state.copy(isLoading = false)
        is LoginChange.Failed -> state.copy(isLoading = false, error = change.error)
    }
}
