package com.ranjan.somiq.auth.ui.login

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object LoginContract {
    data class UiState(
        val email: String = "ranjan@example.com",
        val password: String = "Pass@123",
        val isLoading: Boolean = false,
        val error: Errors? = null
    ) : BaseUiState {
        enum class Errors {
            INVALID_EMAIL,
            INVALID_PASSWORD,
            INVALID_CREDENTIALS,
            LOGIN_FAILED
        }
    }

    sealed interface Action : BaseUiAction {
        data class OnEmailChange(val it: String) : Action
        data class OnPasswordChange(val it: String) : Action
        object Login : Action
        object NavigateToHome : Action
        object NavigateToSignUp : Action
        object ShowError : Action
        object OnGoogleLoginClick : Action
    }

    sealed interface Effect : BaseUiEffect {
        data object NavigateToDashboard : Effect
        data object NavigateToSignUp : Effect
    }
}