package com.basim.block.features.authentication.presentation.login

/** Reduced facts produced by the ViewModel, applied by the reducer. */
sealed interface LoginChange {
    data class EmailUpdated(val value: String) : LoginChange
    data class PasswordUpdated(val value: String) : LoginChange
}

object LoginReducer {
    fun reduce(state: LoginUiState, change: LoginChange): LoginUiState = when (change) {
        is LoginChange.EmailUpdated -> state.copy(email = change.value)
        is LoginChange.PasswordUpdated -> state.copy(password = change.value)
    }
}
