package com.ranjan.somiq.auth.ui.login

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.usecase.LoginUseCase
import com.ranjan.somiq.auth.ui.login.LoginContract.Action
import com.ranjan.somiq.auth.ui.login.LoginContract.Effect
import com.ranjan.somiq.auth.ui.login.LoginContract.UiState
import com.ranjan.somiq.auth.ui.login.mapper.getErrorMessage
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.util.isValidEmail
import com.ranjan.somiq.core.util.isValidPassword
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: com.ranjan.somiq.auth.domain.usecase.LoginUseCase,
    private val globalEffectDispatcher: GlobalEffectDispatcher,
) : BaseViewModel<UiState, Action, Effect>(UiState()) {

    override fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.OnEmailChange -> {
                    setState { copy(email = action.it) }
                }

                is Action.Login -> handleLogin()

                is Action.NavigateToHome -> {
                    emitEffect(Effect.NavigateToDashboard)
                }

                is Action.NavigateToSignUp -> {
                    emitEffect(Effect.NavigateToSignUp)
                }

                is Action.ShowError -> {
                    val error = UiState.Errors.LOGIN_FAILED
                    setState { copy(error = error) }
                    globalEffectDispatcher.emit(
                        GlobalUiEffect.ShowSnackbar(
                            message = error.getErrorMessage(),
                            duration = SnackbarDuration.Short
                        )
                    )
                }

                is Action.OnPasswordChange -> {
                    setState { copy(password = action.it) }
                }

                Action.OnGoogleLoginClick -> {
                    TODO()
                }
            }
        }
    }

    suspend fun handleLogin() {
        val email = state.value.email
        val password = state.value.password

        val isValidEmail = email.isValidEmail()
        val isValidPassword = password.isValidPassword()
        val error = when {
            !isValidEmail && !isValidPassword -> UiState.Errors.INVALID_CREDENTIALS
            !isValidEmail -> UiState.Errors.INVALID_EMAIL
            !isValidPassword -> UiState.Errors.INVALID_PASSWORD
            else -> null
        }
        if (error != null) {
            setState { copy(error = error) }
            globalEffectDispatcher.emit(
                GlobalUiEffect.ShowSnackbar(
                    message = error.getErrorMessage(),
                    duration = SnackbarDuration.Short
                )
            )
            return
        }

        setState { copy(isLoading = true, error = null) }

        val result = loginUseCase(email, password)
        println("RANJAN : $result")
        when (result) {
            is AuthResult.Success -> handleAction(
                Action.NavigateToHome)

            is AuthResult.Failure -> {
                val error = UiState.Errors.LOGIN_FAILED
                setState {
                    copy(
                        error = error, isLoading = false
                    )
                }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(
                        message = error.getErrorMessage(),
                        duration = SnackbarDuration.Short
                    )
                )
            }
        }
    }
}