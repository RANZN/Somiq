package com.ranjan.somiq.auth.ui.otp

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object OtpContract {
    const val MAX_OTP_FAILURES_BEFORE_LOCKOUT = 3

    @Stable
    data class UiState(
        val phoneDisplay: String,
        val otp: String = "",
        val isLoading: Boolean = false,
        val error: Error? = null,
        /** Counts failed verify attempts (local or server); reset when OTP text changes. */
        val failedAttempts: Int = 0,
    ) : BaseUiState {
        enum class Error {
            INVALID_OTP,
            ACCOUNT_NOT_FOUND,
            PHONE_REGISTERED,
            GENERIC,
        }
    }

    sealed interface Intent : BaseUiIntent {
        data class OnOtpChange(val otp: String) : Intent
        data object Verify : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object NavigateHome : Effect
        data class NavigateCompleteProfile(val signupToken: String) : Effect
        /** After too many failed OTP attempts: clear session and return to phone entry. */
        data object NavigateBackToPhone : Effect
        /** Shown via host (e.g. global snackbar); kept out of the ViewModel’s global dispatcher. */
        data class ShowSnackbar(
            val message: String,
            val duration: SnackbarDuration = SnackbarDuration.Short,
        ) : Effect
    }
}
