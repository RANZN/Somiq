package com.ranjan.smartcents.presentation.signup

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.smartcents.domain.model.AuthResult
import com.ranjan.smartcents.domain.repository.AuthRepo
import com.ranjan.smartcents.util.isValidEmail
import com.ranjan.smartcents.util.isValidPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<String>(replay = 1)
    val error = _error.asSharedFlow()

    private val _navigateToHome = MutableSharedFlow<Unit>(replay = 1)
    val navigateToHome = _navigateToHome.asSharedFlow()
    fun handleAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.OnNameChange -> {
                    _uiState.value = _uiState.value.copy(name = action.name)
                }

                is Action.OnEmailChange -> {
                    _uiState.value = _uiState.value.copy(email = action.email)
                }

                is Action.OnPasswordChange -> {
                    _uiState.value = _uiState.value.copy(password = action.password)
                }

                is Action.OnConfirmPasswordChange -> {
                    _uiState.value = _uiState.value.copy(confirmPassword = action.confirmPassword)
                }

                is Action.Signup -> {
                    val errors = validateFields(_uiState.value)
                    if (errors.isNotEmpty()) {
                        _uiState.update { it.copy(error = errors) }
                        return@launch
                    }

                    _uiState.update { it.copy(isLoading = true, error = emptyList()) }
                    val result = authRepo.signUpUser(
                        name = _uiState.value.name,
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )
                    when (result) {
                        AuthResult.Failure.EmailAlreadyInUse -> {
                            _uiState.update {
                                it.copy(
                                    error = listOf(UiState.Error.EMAIL.AlreadyInUse),
                                    isLoading = false
                                )
                            }
                        }

                        is AuthResult.Failure.Unknown -> {
                            _uiState.update {
                                it.copy(
                                    error = listOf(UiState.Error.GenericError(result.message)),
                                    isLoading = false
                                )
                            }
                        }

                        is AuthResult.Success -> {
                            handleAction(Action.NavigateToHome)
                        }
                    }
                }

                is Action.ShowError -> {
                    _error.emit(action.error)
                    _uiState.update {
                        it.copy(
                            error = listOf(UiState.Error.GenericError(action.error)),
                            isLoading = false
                        )
                    }
                }

                is Action.OnGoogleLoginClick -> {
                    // Handle Google login click
                }

                is Action.NavigateToHome -> {
                    _navigateToHome.emit(Unit)
                }

            }
        }
    }

    fun validateFields(state: UiState): List<UiState.Error> {
        val errors = mutableListOf<UiState.Error>()

        // Name validation
        if (state.name.isEmpty()) {
            errors.add(UiState.Error.NAME.Required)
        } else if (state.name.length < 3) {
            errors.add(UiState.Error.NAME.TooShort)
        }

        // Email validation
        if (state.email.isEmpty()) {
            errors.add(UiState.Error.EMAIL.Required)
        } else if (!state.email.isValidEmail()) {
            errors.add(UiState.Error.EMAIL.InvalidFormat)
        }

        // Password validation
        if (state.password.isEmpty()) {
            errors.add(UiState.Error.PASSWORD.Required)
        } else if (!state.password.isValidPassword()) {
            errors.add(UiState.Error.PASSWORD.InvalidFormat)
        }

        // Confirm Password validation
        if (state.confirmPassword.isEmpty()) {
            errors.add(UiState.Error.CONFIRM_PASSWORD.Required)
        } else if (state.password != state.confirmPassword) {
            errors.add(UiState.Error.PASSWORD.Mismatch)
            errors.add(UiState.Error.CONFIRM_PASSWORD.Mismatch)
        }

        return errors
    }

    @Stable
    data class UiState(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val error: List<Error> = emptyList()
    ) {
        sealed interface Error {
            sealed interface NAME : Error {
                object Required : NAME
                object TooShort : NAME
            }

            sealed interface EMAIL : Error {
                object Required : EMAIL
                object InvalidFormat : EMAIL
                object AlreadyInUse : EMAIL
            }

            sealed interface PASSWORD : Error {
                object Required : PASSWORD
                object InvalidFormat : PASSWORD
                object Mismatch : PASSWORD
            }

            sealed interface CONFIRM_PASSWORD : Error {
                object Required : CONFIRM_PASSWORD
                object Mismatch : CONFIRM_PASSWORD
            }

            data class GenericError(val errorMsg: String?) : Error
        }
    }

    sealed interface Action {
        data class OnNameChange(val name: String) : Action
        data class OnEmailChange(val email: String) : Action
        data class OnPasswordChange(val password: String) : Action
        data class OnConfirmPasswordChange(val confirmPassword: String) : Action
        object Signup : Action
        data class ShowError(val error: String) : Action
        object OnGoogleLoginClick : Action
        object NavigateToHome : Action
    }
}