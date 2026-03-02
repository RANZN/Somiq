package com.ranjan.somiq.chat.ui.videocall

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoCallViewModel(
    private val otherUserId: String,
    private val otherUserName: String
) : BaseViewModel<VideoCallContract.UiState, VideoCallContract.Action, VideoCallContract.Effect>() {

    override val initialState: VideoCallContract.UiState
        get() = VideoCallContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName)

    override fun onAction(action: VideoCallContract.Action) {
        viewModelScope.launch {
            when (action) {
                is VideoCallContract.Action.StartCall -> {
                    setState { copy(isConnecting = true) }
                    // TODO: Integrate WebRTC for real video call
                    delay(1500)
                    setState { copy(isConnecting = false, isActive = true) }
                }
                is VideoCallContract.Action.EndCall -> {
                    emitEffect(VideoCallContract.Effect.CallEnded)
                }
                is VideoCallContract.Action.ToggleCamera -> setState { copy(isCameraOn = !isCameraOn) }
                is VideoCallContract.Action.ToggleMic -> setState { copy(isMicOn = !isMicOn) }
            }
        }
    }
}
