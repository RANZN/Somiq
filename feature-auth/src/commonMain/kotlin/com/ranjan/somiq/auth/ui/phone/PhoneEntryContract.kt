package com.ranjan.somiq.auth.ui.phone

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object PhoneEntryContract {
    data class UiState(
        val phone: String = "98765432345",
        val isLoading: Boolean = false,
        val error: Error? = null,
    ) : BaseUiState {
        enum class Error {
            EMPTY,
            INVALID,
        }
    }

    sealed interface Intent : BaseUiIntent {
        data class OnPhoneChange(val phone: String) : Intent
        data object Continue : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToOtp(val phone: String) : Effect
    }
}
