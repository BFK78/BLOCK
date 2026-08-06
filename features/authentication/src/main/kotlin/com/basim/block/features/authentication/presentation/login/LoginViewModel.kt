package com.basim.block.features.authentication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basim.block.core.common.result.DataResult
import com.basim.block.features.authentication.domain.usecase.SignInUseCase
import com.basim.block.features.authentication.domain.validation.PasswordValidator
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
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // One-shot ViewModel → screen notifications. BUFFERED so an event emitted before the
    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginUiAction) {
        when (action) {
            is LoginUiAction.EmailChanged -> apply(LoginChange.EmailUpdated(action.value))
            is LoginUiAction.PasswordChanged -> apply(LoginChange.PasswordUpdated(action.value))
            LoginUiAction.SignInClicked -> signIn()
        }
    }

    fun signIn() {
        val current = _state.value
        if (current.isLoading) return
        apply(LoginChange.Submitting)
        viewModelScope.launch {
            when (val result = signInUseCase(current.email, current.password)) {
                is DataResult.Success -> {
                    apply(LoginChange.Succeeded)
                    _events.send(LoginUiEvent.SignInSucceeded)
                }

                is DataResult.Error -> apply(LoginChange.Failed(result.error))
            }
        }
    }

    /** Single funnel for state changes — always through the pure reducer. */
    private fun apply(change: LoginChange) =
        _state.update { LoginReducer.reduce(it, change) }
}
