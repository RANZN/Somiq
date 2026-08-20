package com.ranjan.somiq.chat.ui.voicecall

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceCallViewModel(
    private val otherUserId: String,
    private val otherUserName: String
) : BaseViewModel<VoiceCallContract.Intent, VoiceCallContract.Effect>() {

    private val _uiState = MutableStateFlow(VoiceCallContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName))
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: VoiceCallContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is VoiceCallContract.Intent.StartCall -> {
                    _uiState.update {it.copy(isConnecting = true) }
                    // TODO: Integrate WebRTC / Call Kit for real voice call
                    delay(1500)
                    _uiState.update {it.copy(isConnecting = false, isActive = true) }
                }
                is VoiceCallContract.Intent.EndCall -> {
                    emitEffect(VoiceCallContract.Effect.CallEnded)
                }
                is VoiceCallContract.Intent.MuteToggle -> { /* TODO: mute/unmute */ }
                is VoiceCallContract.Intent.SpeakerToggle -> { /* TODO: speaker on/off */ }
            }
        }
    }
}
