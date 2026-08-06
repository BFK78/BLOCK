package com.basim.block.features.authentication.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basim.block.core.common.result.DataResult
import com.basim.block.features.authentication.domain.usecase.SignUpUseCase
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val passwordValidator: PasswordValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    // One-shot ViewModel → screen notifications. BUFFERED so an event emitted before the
    // screen collects isn't dropped.
    private val _events = Channel<SignUpUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: SignUpUiAction) {
        when (action) {
            is SignUpUiAction.EmailChanged -> apply(SignUpChange.EmailUpdated(action.value))
            is SignUpUiAction.PasswordChanged -> onPasswordChange(action.value)
            is SignUpUiAction.TermsAcceptedChanged -> apply(SignUpChange.TermsAcceptedUpdated(action.value))
            SignUpUiAction.SignUpClicked -> signUp()
        }
    }

    private fun onPasswordChange(password: String) {
        val passwordValidationResult = passwordValidator(password)
        apply(SignUpChange.PasswordUpdated(value = password, error = passwordValidationResult))
    }

    fun signUp() {
        val current = _state.value
        if (current.isLoading) return
        apply(SignUpChange.Submitting)
        viewModelScope.launch {
            when (val result = signUpUseCase(current.email, current.password)) {
                is DataResult.Success -> {
                    apply(SignUpChange.Succeeded)
                    _events.send(SignUpUiEvent.SignUpSucceeded)
                }

                is DataResult.Error -> apply(SignUpChange.Failed(result.error))
            }
        }
    }

    /** Single funnel for state changes — always through the pure reducer. */
    private fun apply(change: SignUpChange) =
        _state.update { SignUpReducer.reduce(it, change) }
}
