package com.ranjan.somiq.chat.ui.voicecall

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VoiceCallViewModel(
    private val otherUserId: String,
    private val otherUserName: String
) : BaseViewModel<VoiceCallContract.UiState, VoiceCallContract.Intent, VoiceCallContract.Effect>(
    VoiceCallContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName)
) {

    override fun onIntent(intent: VoiceCallContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is VoiceCallContract.Intent.StartCall -> {
                    setState { copy(isConnecting = true) }
                    // TODO: Integrate WebRTC / Call Kit for real voice call
                    delay(1500)
                    setState { copy(isConnecting = false, isActive = true) }
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
