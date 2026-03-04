package com.ranjan.somiq.auth.ui.login

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
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

    sealed interface Intent : BaseUiIntent {
        data class OnEmailChange(val it: String) : Intent
        data class OnPasswordChange(val it: String) : Intent
        object Login : Intent
        object NavigateToHome : Intent
        object NavigateToSignUp : Intent
        object ShowError : Intent
        object OnGoogleLoginClick : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object NavigateToDashboard : Effect
        data object NavigateToSignUp : Effect
    }
}