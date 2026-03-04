package com.ranjan.somiq.auth.ui.login

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.ui.login.LoginContract.Intent
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
) : BaseViewModel<UiState, Intent, Effect>() {

    override val initialState: UiState
        get() = UiState()

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.OnEmailChange -> {
                    setState { copy(email = intent.it) }
                }

                is Intent.Login -> handleLogin()

                is Intent.NavigateToHome -> {
                    emitEffect(Effect.NavigateToDashboard)
                }

                is Intent.NavigateToSignUp -> {
                    emitEffect(Effect.NavigateToSignUp)
                }

                is Intent.ShowError -> {
                    val error = UiState.Errors.LOGIN_FAILED
                    setState { copy(error = error) }
                    globalEffectDispatcher.emit(
                        GlobalUiEffect.ShowSnackbar(
                            message = error.getErrorMessage(),
                            duration = SnackbarDuration.Short
                        )
                    )
                }

                is Intent.OnPasswordChange -> {
                    setState { copy(password = intent.it) }
                }

                Intent.OnGoogleLoginClick -> {
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
            is AuthResult.Success -> handleIntent(
                Intent.NavigateToHome)

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