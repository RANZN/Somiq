package com.ranjan.somiq.chat.ui.videocall

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoCallViewModel(
    private val otherUserId: String,
    private val otherUserName: String
) : BaseViewModel<VideoCallContract.Intent, VideoCallContract.Effect>() {

    private val _uiState = MutableStateFlow(VideoCallContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName))
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: VideoCallContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is VideoCallContract.Intent.StartCall -> {
                    _uiState.update {it.copy(isConnecting = true) }
                    // TODO: Integrate WebRTC for real video call
                    delay(1500)
                    _uiState.update {it.copy(isConnecting = false, isActive = true) }
                }
                is VideoCallContract.Intent.EndCall -> {
                    emitEffect(VideoCallContract.Effect.CallEnded)
                }
                is VideoCallContract.Intent.ToggleCamera -> _uiState.update {it.copy(isCameraOn = !it.isCameraOn) }
                is VideoCallContract.Intent.ToggleMic -> _uiState.update {it.copy(isMicOn = !it.isMicOn) }
            }
        }
    }
}
