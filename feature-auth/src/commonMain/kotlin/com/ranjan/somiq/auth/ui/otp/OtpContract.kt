package com.ranjan.somiq.auth.ui.otp

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.effect.ShowSnackbarEffect
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.enter_6_digit_code
import com.ranjan.somiq.core.resources.error_something_went_wrong
import com.ranjan.somiq.core.resources.invalid_otp_code
import com.ranjan.somiq.core.resources.no_account_for_number
import com.ranjan.somiq.core.resources.phone_already_registered

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
            OTP_INCOMPLETE,
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
        data class ShowSnackbar(override val message: UiText) : Effect, ShowSnackbarEffect
    }
}

fun OtpContract.UiState.Error.getMessage(): UiText = when (this) {
    OtpContract.UiState.Error.OTP_INCOMPLETE -> UiText.Resource(Res.string.enter_6_digit_code)
    OtpContract.UiState.Error.INVALID_OTP -> UiText.Resource(Res.string.invalid_otp_code)
    OtpContract.UiState.Error.ACCOUNT_NOT_FOUND -> UiText.Resource(Res.string.no_account_for_number)
    OtpContract.UiState.Error.PHONE_REGISTERED -> UiText.Resource(Res.string.phone_already_registered)
    OtpContract.UiState.Error.GENERIC -> UiText.Resource(Res.string.error_something_went_wrong)
}
