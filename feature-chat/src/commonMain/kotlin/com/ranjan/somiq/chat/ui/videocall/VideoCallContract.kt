package com.ranjan.somiq.chat.ui.videocall

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
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

    sealed interface Action : BaseUiAction {
        data object StartCall : Action
        data object EndCall : Action
        data object ToggleCamera : Action
        data object ToggleMic : Action
    }

    sealed interface Effect : BaseUiEffect {
        data object CallEnded : Effect
    }
}
