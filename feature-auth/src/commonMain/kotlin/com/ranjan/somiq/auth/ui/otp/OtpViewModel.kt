package com.ranjan.somiq.auth.ui.otp

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.VerifyOtpResult
import com.ranjan.somiq.auth.domain.usecase.VerifyOtpUseCase
import com.ranjan.somiq.auth.ui.otp.OtpContract.Effect
import com.ranjan.somiq.auth.ui.otp.OtpContract.Intent
import com.ranjan.somiq.auth.ui.otp.OtpContract.UiState
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.network.TokenProvider
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class OtpViewModel(
    private val phone: String,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val tokenProvider: TokenProvider,
    private val authStateManager: AuthStateManager,
) : BaseViewModel<UiState, Intent, Effect>(UiState(phoneDisplay = phone)) {

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.OnOtpChange -> setState {
                    copy(otp = intent.otp, error = null, failedAttempts = 0)
                }
                Intent.Verify -> verify()
            }
        }
    }

    private suspend fun verify() {
        val otp = state.value.otp.trim()
        if (otp.length != 6 || !otp.all { it.isDigit() }) {
            if (registerFailure(UiState.Error.INVALID_OTP)) return
            emitEffect(Effect.ShowSnackbar("Enter the 6-digit code"))
            return
        }

        setState { copy(isLoading = true, error = null) }
        when (val result = verifyOtpUseCase(phone, otp)) {
            is VerifyOtpResult.LoggedIn -> {
                setState { copy(isLoading = false) }
                emitEffect(Effect.NavigateHome)
            }

            is VerifyOtpResult.SignupRequired -> {
                setState { copy(isLoading = false) }
                emitEffect(Effect.NavigateCompleteProfile(result.signupToken))
            }

            is VerifyOtpResult.Failure.InvalidOtp -> {
                if (registerFailure(UiState.Error.INVALID_OTP)) return
                emitEffect(Effect.ShowSnackbar("Invalid code"))
            }

            is VerifyOtpResult.Failure.AccountNotFound -> {
                if (registerFailure(UiState.Error.ACCOUNT_NOT_FOUND)) return
                emitEffect(Effect.ShowSnackbar("No account for this number."))
            }

            is VerifyOtpResult.Failure.PhoneAlreadyRegistered -> {
                if (registerFailure(UiState.Error.PHONE_REGISTERED)) return
                emitEffect(Effect.ShowSnackbar("This number is already registered."))
            }

            is VerifyOtpResult.Failure.NoNetwork -> {
                setState { copy(isLoading = false) }
                emitEffect(Effect.ShowSnackbar("No network"))
            }

            is VerifyOtpResult.Failure.ServerError,
            is VerifyOtpResult.Failure.Unknown -> {
                if (registerFailure(UiState.Error.GENERIC)) return
                emitEffect(Effect.ShowSnackbar("Something went wrong"))
            }
        }
    }

    /**
     * Increments failure count; on the 3rd failure clears tokens, logs out locally, and navigates back to phone.
     * @return true if lockout was triggered (caller should not show the usual error snackbar).
     */
    private suspend fun registerFailure(error: UiState.Error?): Boolean {
        val next = state.value.failedAttempts + 1
        setState { copy(isLoading = false, failedAttempts = next, error = error) }
        if (next >= OtpContract.MAX_OTP_FAILURES_BEFORE_LOCKOUT) {
            clearSessionAndNavigateBack()
            return true
        }
        return false
    }

    private suspend fun clearSessionAndNavigateBack() {
        tokenProvider.clearToken()
        authStateManager.setLoggedIn(false)
        setState { copy(isLoading = false, failedAttempts = 0, error = null) }
        emitEffect(Effect.NavigateBackToPhone)
        emitEffect(
            Effect.ShowSnackbar(
                "Too many failed attempts. Enter your number again to get a new code.",
                duration = SnackbarDuration.Long,
            )
        )
    }
}
