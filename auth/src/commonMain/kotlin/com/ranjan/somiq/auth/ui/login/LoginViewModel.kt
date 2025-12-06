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
    private val loginUseCase: com.ranjan.somiq.auth.domain.usecase.LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(capacity = Channel.CONFLATED)
    val events = _events.receiveAsFlow()

    fun handleAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.OnEmailChange -> {
                    _uiState.update { it.copy(email = action.it) }
                }

                is LoginAction.Login -> handleLogin()

                is LoginAction.NavigateToHome -> {
                    _events.send(LoginEvent.NavigateToDashboard)
                }

                is LoginAction.NavigateToSignUp -> {
                    _events.send(LoginEvent.NavigateToSignUp)
                }

                is LoginAction.ShowError -> {
                    _uiState.update { it.copy(error = LoginUiState.Errors.LOGIN_FAILED) }
                }

                is LoginAction.OnPasswordChange -> {
                    _uiState.update { it.copy(password = action.it) }
                }

                LoginAction.OnGoogleLoginClick -> {
                    TODO()
                }
            }
        }
    }

    suspend fun handleLogin() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        val isValidEmail = email.isValidEmail()
        val isValidPassword = password.isValidPassword()
        val error = when {
            !isValidEmail && !isValidPassword -> LoginUiState.Errors.INVALID_CREDENTIALS
            !isValidEmail -> LoginUiState.Errors.INVALID_EMAIL
            !isValidPassword -> LoginUiState.Errors.INVALID_PASSWORD
            else -> null
        }
        if (error != null) {
            _uiState.update { it.copy(error = error) }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val result = loginUseCase(email, password)
        println("RANJAN : $result")
        when (result) {
            is AuthResult.Success -> handleAction(
                LoginAction.NavigateToHome)

            is AuthResult.Failure -> {
                _uiState.update {
                    it.copy(
                        error = LoginUiState.Errors.LOGIN_FAILED, isLoading = false
                    )
                }
            }
        }
    }
}