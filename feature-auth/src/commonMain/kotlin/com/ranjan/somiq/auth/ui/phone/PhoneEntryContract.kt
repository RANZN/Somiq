package com.ranjan.somiq.auth.ui.phone

import com.ranjan.somiq.core.presentation.effect.ShowSnackbarEffect
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.enter_valid_phone_numberr
import com.ranjan.somiq.core.resources.enter_your_phone_number

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
        data class ShowSnackbar(override val message: UiText) : Effect, ShowSnackbarEffect
    }
}

fun PhoneEntryContract.UiState.Error.getMessage(): UiText = when (this) {
    PhoneEntryContract.UiState.Error.EMPTY -> UiText.Resource(Res.string.enter_your_phone_number)
    PhoneEntryContract.UiState.Error.INVALID -> UiText.Resource(Res.string.enter_valid_phone_numberr)
}
