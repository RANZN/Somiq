package com.ranjan.somiq.chat.ui.videocall

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object VideoCallContract {
    data class UiState(
        val otherUserId: String = "",
        val otherUserName: String = "",
        val isConnecting: Boolean = false,
        val isActive: Boolean = false,
        val isCameraOn: Boolean = true,
        val isMicOn: Boolean = true,
        val error: String? = null
    ) : BaseUiState

    sealed interface Intent : BaseUiIntent {
        data object StartCall : Intent
        data object EndCall : Intent
        data object ToggleCamera : Intent
        data object ToggleMic : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data object CallEnded : Effect
    }
}
