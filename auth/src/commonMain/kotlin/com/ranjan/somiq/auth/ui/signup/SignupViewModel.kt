package com.ranjan.somiq.auth.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.usecase.SignupUseCase
import com.ranjan.somiq.core.util.isValidEmail
import com.ranjan.somiq.core.util.isValidPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val signupUseCase: SignupUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<SignUpEvent>(capacity = Channel.CONFLATED)
    val events = _events.receiveAsFlow()

    fun handleAction(action: SignUpAction) {
        viewModelScope.launch {
            when (action) {
                is SignUpAction.OnNameChange -> {
                    _uiState.value = _uiState.value.copy(name = action.name)
                }

                is SignUpAction.OnEmailChange -> {
                    _uiState.value = _uiState.value.copy(email = action.email)
                }

                is SignUpAction.OnPasswordChange -> {
                    _uiState.value = _uiState.value.copy(password = action.password)
                }

                is SignUpAction.OnConfirmPasswordChange -> {
                    _uiState.value = _uiState.value.copy(confirmPassword = action.confirmPassword)
                }

                is SignUpAction.Signup -> {
                    val errors = validateFields(_uiState.value)
                    if (errors.isNotEmpty()) {
                        _uiState.update { it.copy(error = errors) }
                        return@launch
                    }

                    _uiState.update { it.copy(isLoading = true, error = emptyList()) }
                    val result = signupUseCase(
                        name = _uiState.value.name,
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )
                    when (result) {
                        AuthResult.Failure.EmailAlreadyInUse -> {
                            _uiState.update {
                                it.copy(
                                    error = listOf(SignUpUiState.Error.Email.AlreadyInUse),
                                    isLoading = false
                                )
                            }
                        }

                        is AuthResult.Failure.Unknown -> {
                            _uiState.update {
                                it.copy(
                                    error = listOf(SignUpUiState.Error.GenericError(result.message)),
                                    isLoading = false
                                )
                            }
                        }

                        is AuthResult.Success -> {
                            handleAction(SignUpAction.NavigateToHome)
                        }

                        else -> {}
                    }
                }

                is SignUpAction.ShowError -> {
                    _uiState.update {
                        it.copy(
                            error = listOf(SignUpUiState.Error.GenericError(action.error)),
                            isLoading = false
                        )
                    }
                }

                is SignUpAction.OnGoogleLoginClick -> {
                    // Handle Google login click
                }

                is SignUpAction.NavigateToHome -> {
                    _events.send(SignUpEvent.NavigateToHome)
                }

            }
        }
    }

    fun validateFields(state: SignUpUiState): List<SignUpUiState.Error> {
        val errors = mutableListOf<SignUpUiState.Error>()

        // Name validation
        if (state.name.isEmpty()) {
            errors.add(SignUpUiState.Error.Name.Required)
        } else if (state.name.length < 3) {
            errors.add(SignUpUiState.Error.Name.TooShort)
        }

        // Email validation
        if (state.email.isEmpty()) {
            errors.add(SignUpUiState.Error.Email.Required)
        } else if (!state.email.isValidEmail()) {
            errors.add(SignUpUiState.Error.Email.InvalidFormat)
        }

        // Password validation
        if (state.password.isEmpty()) {
            errors.add(SignUpUiState.Error.Password.Required)
        } else if (!state.password.isValidPassword()) {
            errors.add(SignUpUiState.Error.Password.InvalidFormat)
        }

        // Confirm Password validation
        if (state.confirmPassword.isEmpty()) {
            errors.add(SignUpUiState.Error.ConfirmPassword.Required)
        } else if (state.password != state.confirmPassword) {
            errors.add(SignUpUiState.Error.Password.Mismatch)
            errors.add(SignUpUiState.Error.ConfirmPassword.Mismatch)
        }

        return errors
    }

}