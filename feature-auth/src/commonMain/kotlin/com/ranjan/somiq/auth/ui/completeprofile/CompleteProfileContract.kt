package com.ranjan.somiq.auth.ui.completeprofile

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object CompleteProfileContract {
    @Stable
    data class UiState(
        val name: String = "",
        val userId: String = "",
        val email: String = "",
        val profilePictureUrl: String? = null,
        val isLoading: Boolean = false,
        /** Null until check-user-id runs for the current username (debounce or focus lost). */
        val userIdAvailable: Boolean? = null,
        val isCheckingUserId: Boolean = false,
        val error: List<Error> = emptyList(),
    ) : BaseUiState {
        sealed interface Error {
            sealed interface Name : Error {
                object Required : Name
                object TooShort : Name
            }

            sealed interface UserId : Error {
                object Required : UserId
                object TooShort : UserId
                object InvalidFormat : UserId
                object AlreadyInUse : UserId
                /** Username format is valid but availability hasn’t been confirmed yet. */
                object AvailabilityNotChecked : UserId
                object Unavailable : UserId
            }

            sealed interface Email : Error {
                object InvalidFormat : Email
            }

            data class GenericError(val errorMsg: String?) : Error
        }
    }

    sealed interface Intent : BaseUiIntent {
        data class OnNameChange(val name: String) : Intent
        data class OnUserIdChange(val userId: String) : Intent
        data class OnUserIdFocusChanged(val isFocused: Boolean) : Intent
        data class OnEmailChange(val email: String) : Intent
        data object AddPhotoClick : Intent
        data object Submit : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object NavigateHome : Effect
    }
}
