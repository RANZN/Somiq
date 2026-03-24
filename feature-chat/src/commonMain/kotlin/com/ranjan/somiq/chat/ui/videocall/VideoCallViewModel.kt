package com.ranjan.somiq.chat.ui.videocall

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoCallViewModel(
    private val otherUserId: String,
    private val otherUserName: String
) : BaseViewModel<VideoCallContract.UiState, VideoCallContract.Intent, VideoCallContract.Effect>(
    VideoCallContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName)
) {

    override fun onIntent(intent: VideoCallContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is VideoCallContract.Intent.StartCall -> {
                    setState { copy(isConnecting = true) }
                    // TODO: Integrate WebRTC for real video call
                    delay(1500)
                    setState { copy(isConnecting = false, isActive = true) }
                }
                is VideoCallContract.Intent.EndCall -> {
                    emitEffect(VideoCallContract.Effect.CallEnded)
                }
                is VideoCallContract.Intent.ToggleCamera -> setState { copy(isCameraOn = !isCameraOn) }
                is VideoCallContract.Intent.ToggleMic -> setState { copy(isMicOn = !isMicOn) }
            }
        }
    }
}
