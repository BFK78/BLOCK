package com.basim.block.features.authentication.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // One-shot ViewModel → screen notifications. BUFFERED so an event emitted before the
    // screen collects isn't dropped. Nothing is emitted yet (hold state only).
    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginUiAction) {
        when (action) {
            is LoginUiAction.EmailChanged -> apply(LoginChange.EmailUpdated(action.value))
            is LoginUiAction.PasswordChanged -> apply(LoginChange.PasswordUpdated(action.value))
        }
    }

    /** Single funnel for state changes — always through the pure reducer. */
    private fun apply(change: LoginChange) =
        _state.update { LoginReducer.reduce(it, change) }
}
