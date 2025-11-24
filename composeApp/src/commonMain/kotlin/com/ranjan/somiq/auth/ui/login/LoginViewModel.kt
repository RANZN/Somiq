package com.ranjan.somiq.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.usecase.LoginUseCase
import com.ranjan.somiq.core.util.isValidEmail
import com.ranjan.somiq.core.util.isValidPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _navigateToHome = Channel<Unit>(Channel.Factory.BUFFERED)
    val navigateToHome = _navigateToHome.receiveAsFlow()

    fun handleAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.OnEmailChange -> {
                    _uiState.update { it.copy(email = action.it) }
                }

                is LoginAction.Login -> handleLogin()

                is LoginAction.NavigateToHome -> {
                    _navigateToHome.send(Unit)
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

    suspend fun handleLogin() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        val isValidEmail = email.isValidEmail()
        val isValidPassword = password.isValidPassword()
        val error = when {
            !isValidEmail && !isValidPassword -> LoginState.Errors.INVALID_CREDENTIALS
            !isValidEmail -> LoginState.Errors.INVALID_EMAIL
            !isValidPassword -> LoginState.Errors.INVALID_PASSWORD
            else -> null
        }
        if (error != null) {
            _uiState.update { it.copy(error = error) }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val result = loginUseCase(email, password)
        when (result) {
            is AuthResult.Success -> handleAction(LoginAction.NavigateToHome)

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

    data class LoginState(
        val email: String = "ranjan@yopmail.com",
        val password: String = "Ranjan@123",
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