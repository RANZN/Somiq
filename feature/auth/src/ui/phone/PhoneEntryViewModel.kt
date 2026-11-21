package com.ranjan.somiq.auth.ui.phone

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Effect
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.Intent
import com.ranjan.somiq.auth.ui.phone.PhoneEntryContract.UiState
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.util.isValidPhone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneEntryViewModel : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.OnPhoneChange -> _uiState.update {it.copy(phone = intent.phone, error = null) }
                Intent.Continue -> handleContinue()
            }
        }
    }
    private fun handleContinue() {
        val p = uiState.value.phone.trim()
        val err = when {
            p.isEmpty() -> UiState.Error.EMPTY
            !p.isValidPhone() -> UiState.Error.INVALID
            else -> null
        }
        if (err != null) {
            _uiState.update {it.copy(error = err) }
            showSnackbar(err.getMessage())
            return
        }
        emitEffect(Effect.NavigateToOtp(p))
    }
}
