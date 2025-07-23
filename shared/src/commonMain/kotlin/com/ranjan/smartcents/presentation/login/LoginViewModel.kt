package com.ranjan.smartcents.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.smartcents.domain.model.AuthResult
import com.ranjan.smartcents.domain.repository.AuthRepo
import com.ranjan.smartcents.util.isValidEmail
import com.ranjan.smartcents.util.isValidPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _navigateToHome = MutableSharedFlow<Unit>(replay = 1)
    val navigateToHome = _navigateToHome.asSharedFlow()

    fun handleAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.OnEmailChange -> {
                    _uiState.update { it.copy(email = action.it) }
                }

                is LoginAction.Login -> {
                    val isValidEmail = _uiState.value.email.isValidEmail()
                    val isValidPassword = _uiState.value.password.isValidPassword()
                    val error = when {
                        !isValidEmail && !isValidPassword -> LoginState.Errors.INVALID_CREDENTIALS
                        !isValidEmail -> LoginState.Errors.INVALID_EMAIL
                        !isValidPassword -> LoginState.Errors.INVALID_PASSWORD
                        else -> null
                    }
                    if (error != null) {
                        _uiState.update { it.copy(error = error) }
                        return@launch
                    }

                    _uiState.update { it.copy(isLoading = true, error = null) }

                    val result = authRepo.loginUser(_uiState.value.email, _uiState.value.password)
                    when (result) {
                        is AuthResult.Success -> {
                            handleAction(LoginAction.NavigateToHome)
                        }

                        is AuthResult.Failure -> {
                            _uiState.update {
                                it.copy(
                                    error = LoginState.Errors.LOGIN_FAILED,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }

                is LoginAction.NavigateToHome -> {
                    _navigateToHome.emit(Unit)
                }

                is LoginAction.ShowError -> {
                    _uiState.update { it.copy(error = LoginState.Errors.LOGIN_FAILED) }
                }

                is LoginAction.OnPasswordChange -> {
                    _uiState.update { it.copy(password = action.it) }
                }

                else -> Unit
            }
        }
    }

    data class LoginState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: Errors? = null
    ) {
        enum class Errors {
            INVALID_EMAIL,
            INVALID_PASSWORD,
            INVALID_CREDENTIALS,
            LOGIN_FAILED
        }
    }

    sealed interface LoginAction {
        data class OnEmailChange(val it: String) : LoginAction
        data class OnPasswordChange(val it: String) : LoginAction
        object Login : LoginAction
        object NavigateToHome : LoginAction
        object NavigateToSignUp : LoginAction
        object ShowError : LoginAction
        object OnGoogleLoginClick : LoginAction
    }
}