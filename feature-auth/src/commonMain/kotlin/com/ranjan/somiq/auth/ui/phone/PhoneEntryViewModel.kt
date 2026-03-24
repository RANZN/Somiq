package com.ranjan.somiq.auth.ui.phone

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Effect
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Intent
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.UiState
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.util.isValidPhone
import kotlinx.coroutines.launch

class PhoneEntryViewModel(
    private val globalEffectDispatcher: GlobalEffectDispatcher,
) : BaseViewModel<UiState, Intent, Effect>(UiState()) {

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.OnPhoneChange -> setState { copy(phone = intent.phone, error = null) }
                Intent.Continue -> handleContinue()
            }
        }
    }

    private suspend fun handleContinue() {
        val p = state.value.phone.trim()
        val err = when {
            p.isEmpty() -> UiState.Error.EMPTY
            !p.isValidPhone() -> UiState.Error.INVALID
            else -> null
        }
        if (err != null) {
            setState { copy(error = err) }
            val msg = when (err) {
                UiState.Error.EMPTY -> "Enter your phone number"
                UiState.Error.INVALID -> "Enter a valid phone number (10–15 digits)"
            }
            globalEffectDispatcher.emit(
                GlobalUiEffect.ShowSnackbar(message = msg, duration = SnackbarDuration.Short)
            )
            return
        }
        emitEffect(Effect.NavigateToOtp(p))
    }
}
