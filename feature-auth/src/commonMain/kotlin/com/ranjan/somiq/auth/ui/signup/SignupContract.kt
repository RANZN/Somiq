package com.ranjan.somiq.auth.ui.signup

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object SignUpContract {
    @Stable
    data class UiState(
        val name: String = "",
        val username: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val error: List<Error> = emptyList()
    ) : BaseUiState {
        sealed interface Error {
            sealed interface Name : Error {
                object Required : Name
                object TooShort : Name
            }

            sealed interface Username : Error {
                object Required : Username
                object TooShort : Username
                object InvalidFormat : Username
                object AlreadyInUse : Username
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

    sealed interface Intent : BaseUiIntent {
        data object AddPictureClick : Intent
        data class OnNameChange(val name: String) : Intent
        data class OnUsernameChange(val username: String) : Intent
        data class OnEmailChange(val email: String) : Intent
        data class OnPasswordChange(val password: String) : Intent
        data class OnConfirmPasswordChange(val confirmPassword: String) : Intent
        object Signup : Intent
        data class ShowError(val error: String) : Intent
        object OnGoogleLoginClick : Intent
        object NavigateToHome : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object NavigateToHome : Effect
    }
}