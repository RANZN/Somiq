package com.ranjan.somiq.auth.ui.login


data class LoginUiState(
    val email: String = "ranjan@example.com",
    val password: String = "Pass@123",
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

sealed interface LoginEvent {
    data object NavigateToDashboard : LoginEvent
    data object NavigateToSignUp : LoginEvent
}