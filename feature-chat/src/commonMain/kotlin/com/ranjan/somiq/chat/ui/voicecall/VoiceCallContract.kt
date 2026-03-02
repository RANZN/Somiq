package com.ranjan.somiq.chat.ui.voicecall

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object VoiceCallContract {
    data class UiState(
        val otherUserId: String = "",
        val otherUserName: String = "",
        val isConnecting: Boolean = false,
        val isActive: Boolean = false,
        val error: String? = null
    ) : BaseUiState

    sealed interface Action : BaseUiAction {
        data object StartCall : Action
        data object EndCall : Action
        data object MuteToggle : Action
        data object SpeakerToggle : Action
    }

    sealed interface Effect : BaseUiEffect {
        data object CallEnded : Effect
    }
}
