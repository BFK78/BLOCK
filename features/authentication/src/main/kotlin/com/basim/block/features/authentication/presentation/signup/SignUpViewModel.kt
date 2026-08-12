package com.basim.block.features.authentication.presentation.signup

import android.util.Patterns
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

        val emailInvalid = Patterns.EMAIL_ADDRESS.matcher(current.email).matches().not()
        if (emailInvalid) {
            apply(SignUpChange.InvalidEmail)
        }

        val passwordInvalid = passwordValidator(current.password).isValid.not()
        if (passwordInvalid) {
            apply(SignUpChange.InvalidPassword)
        }

        if (emailInvalid || passwordInvalid) return

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

    private fun apply(change: SignUpChange) =
        _state.update { SignUpReducer.reduce(it, change) }
}
