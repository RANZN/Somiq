package com.ranjan.smartcents.presentation.signup

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.smartcents.domain.model.AuthResult
import com.ranjan.smartcents.domain.repository.AuthRepo
import com.ranjan.smartcents.res.strings
import com.ranjan.smartcents.util.ErrorState
import com.ranjan.smartcents.util.isValidEmail
import com.ranjan.smartcents.util.isValidPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepo: AuthRepo
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
                                    error = listOf(
                                        ErrorState(
                                            UiState.Error.EMAIL,
                                            strings.emailAlreadyInUse
                                        )
                                    ),
                                    isLoading = false
                                )
                            }
                        }

                        is AuthResult.Failure.Unknown -> {
                            handleAction(
                                Action.ShowError(
                                    result.message ?: strings.somethingWentWring
                                )
                            )
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
                            error = listOf(
                                ErrorState(
                                    UiState.Error.SIGNUP,
                                    action.error
                                )
                            ),
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

    fun validateFields(state: UiState): List<ErrorState<UiState.Error>> {
        val errors = mutableListOf<ErrorState<UiState.Error>>()

        if (state.name.isEmpty()) {
            errors.add(ErrorState(UiState.Error.NAME, strings.nameRequired))
        } else if (state.name.length < 3) {
            errors.add(ErrorState(UiState.Error.NAME, strings.nameTooShort))
        }

        if (state.email.isEmpty()) {
            errors.add(ErrorState(UiState.Error.EMAIL, strings.emailRequired))
        } else if (!state.email.isValidEmail()) {
            errors.add(ErrorState(UiState.Error.EMAIL, strings.invalidEmail))
        }

        if (state.password.isEmpty()) {
            errors.add(ErrorState(UiState.Error.PASSWORD, strings.passwordRequired))
        } else if (!state.password.isValidPassword()) {
            errors.add(ErrorState(UiState.Error.PASSWORD, strings.invalidPassword))
        }

        if (state.confirmPassword.isEmpty()) {
            errors.add(ErrorState(UiState.Error.RE_PASSWORD, strings.confirmPasswordRequired))
        } else if (state.password != state.confirmPassword) {
            errors.add(ErrorState(UiState.Error.PASSWORD))
            errors.add(ErrorState(UiState.Error.RE_PASSWORD, strings.passwordsDoNotMatch))
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
        val error: List<ErrorState<Error>> = emptyList()
    ) {
        enum class Error {
            EMAIL,
            NAME,
            PASSWORD,
            RE_PASSWORD,
            SIGNUP
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