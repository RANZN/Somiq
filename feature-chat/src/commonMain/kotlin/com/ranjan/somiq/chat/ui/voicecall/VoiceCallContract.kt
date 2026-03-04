package com.ranjan.somiq.chat.ui.voicecall

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
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

    sealed interface Intent : BaseUiIntent {
        data object StartCall : Intent
        data object EndCall : Intent
        data object MuteToggle : Intent
        data object SpeakerToggle : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object CallEnded : Effect
    }
}
