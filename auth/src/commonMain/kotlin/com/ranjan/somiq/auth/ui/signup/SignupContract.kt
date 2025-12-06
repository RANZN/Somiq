package com.ranjan.somiq.auth.ui.signup

import androidx.compose.runtime.Stable

@Stable
data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: List<Error> = emptyList()
) {
    sealed interface Error {
        sealed interface Name : Error {
            object Required : Name
            object TooShort : Name
        }

        sealed interface Email : Error {
            object Required : Email
            object InvalidFormat : Email
            object AlreadyInUse : Email
        }

        sealed interface Password : Error {
            object Required : Password
            object InvalidFormat : Password
            object Mismatch : Password
        }

        sealed interface ConfirmPassword : Error {
            object Required : ConfirmPassword
            object Mismatch : ConfirmPassword
        }

        data class GenericError(val errorMsg: String?) : Error
    }
}

sealed interface SignUpAction {
    data class OnNameChange(val name: String) : SignUpAction
    data class OnEmailChange(val email: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : SignUpAction
    object Signup : SignUpAction
    data class ShowError(val error: String) : SignUpAction
    object OnGoogleLoginClick : SignUpAction
    object NavigateToHome : SignUpAction
}

sealed interface SignUpEvent {
    data object NavigateToHome : SignUpEvent
}