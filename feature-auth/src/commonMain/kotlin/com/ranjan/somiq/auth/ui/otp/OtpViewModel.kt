package com.ranjan.somiq.auth.ui.otp

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.VerifyOtpResult
import com.ranjan.somiq.auth.domain.usecase.VerifyOtpUseCase
import com.ranjan.somiq.auth.ui.otp.OtpContract.Effect
import com.ranjan.somiq.auth.ui.otp.OtpContract.Intent
import com.ranjan.somiq.auth.ui.otp.OtpContract.UiState
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.network.TokenProvider
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_no_internet
import com.ranjan.somiq.core.resources.otp_too_many_failed_attempts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel(
    private val phone: String,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val tokenProvider: TokenProvider,
    private val authStateManager: AuthStateManager,
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState(phoneDisplay = phone))
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.OnOtpChange -> {
                if (uiState.value.isSixDigitOtp) return
                _uiState.update {it.copy(otp = intent.otp, error = null, failedAttempts = 0)
                }
            }
            Intent.Verify -> viewModelScope.launch {
                verify()
            }
        }
    }
    private suspend fun verify() {
        val otp = uiState.value.otp.trim()
        if (otp.length != 6 || !otp.all { it.isDigit() }) {
            val err = UiState.Error.OTP_INCOMPLETE
            if (registerFailure(err)) return
            showSnackbar(err.getMessage())
            return
        }
        _uiState.update {it.copy(isLoading = true, error = null) }
        when (val result = verifyOtpUseCase(phone, otp)) {
            is VerifyOtpResult.LoggedIn -> {
                _uiState.update {it.copy(isLoading = false) }
                emitEffect(Effect.NavigateHome)
            }
            is VerifyOtpResult.SignupRequired -> {
                _uiState.update {it.copy(isLoading = false) }
                emitEffect(Effect.NavigateCompleteProfile(result.signupToken))
            }
            is VerifyOtpResult.Failure.InvalidOtp -> {
                val err = UiState.Error.INVALID_OTP
                if (registerFailure(err)) return
                showSnackbar(err.getMessage())
            }
            is VerifyOtpResult.Failure.AccountNotFound -> {
                val err = UiState.Error.ACCOUNT_NOT_FOUND
                if (registerFailure(err)) return
                showSnackbar(err.getMessage())
            }
            is VerifyOtpResult.Failure.PhoneAlreadyRegistered -> {
                val err = UiState.Error.PHONE_REGISTERED
                if (registerFailure(err)) return
                showSnackbar(err.getMessage())
            }
            is VerifyOtpResult.Failure.NoNetwork -> {
                _uiState.update {it.copy(isLoading = false) }
                showSnackbar(UiText.Resource(Res.string.error_no_internet))
            }
            is VerifyOtpResult.Failure.ServerError,
            is VerifyOtpResult.Failure.Unknown -> {
                val err = UiState.Error.GENERIC
                if (registerFailure(err)) return
                showSnackbar(err.getMessage())
            }
        }
    }
    /**
     * Increments failure count; on the 3rd failure clears tokens, logs out locally, and navigates back to phone.
     * @return true if lockout was triggered (caller should not show the usual error snackbar).
     */
    private suspend fun registerFailure(error: UiState.Error?): Boolean {
        val next = uiState.value.failedAttempts + 1
        _uiState.update {it.copy(isLoading = false, failedAttempts = next, error = error) }
        if (next >= OtpContract.MAX_OTP_FAILURES_BEFORE_LOCKOUT) {
            clearSessionAndNavigateBack()
            return true
        }
        return false
    }
    private suspend fun clearSessionAndNavigateBack() {
        tokenProvider.clearToken()
        authStateManager.clearUserId()
        _uiState.update {it.copy(isLoading = false, failedAttempts = 0, error = null) }
        emitEffect(Effect.NavigateBackToPhone)
        showSnackbar(UiText.Resource(Res.string.otp_too_many_failed_attempts))
    }
}
