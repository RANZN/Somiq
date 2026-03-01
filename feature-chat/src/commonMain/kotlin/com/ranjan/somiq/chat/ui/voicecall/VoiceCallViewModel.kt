package com.ranjan.somiq.chat.ui.voicecall

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VoiceCallViewModel(
    private val otherUserId: String,
    private val otherUserName: String
) : BaseViewModel<VoiceCallContract.UiState, VoiceCallContract.Action, VoiceCallContract.Effect>() {

    override val initialState: VoiceCallContract.UiState
        get() = VoiceCallContract.UiState(otherUserId = otherUserId, otherUserName = otherUserName)

    override fun onAction(action: VoiceCallContract.Action) {
        viewModelScope.launch {
            when (action) {
                is VoiceCallContract.Action.StartCall -> {
                    setState { copy(isConnecting = true) }
                    // TODO: Integrate WebRTC / Call Kit for real voice call
                    delay(1500)
                    setState { copy(isConnecting = false, isActive = true) }
                }
                is VoiceCallContract.Action.EndCall -> {
                    emitEffect(VoiceCallContract.Effect.CallEnded)
                }
                is VoiceCallContract.Action.MuteToggle -> { /* TODO: mute/unmute */ }
                is VoiceCallContract.Action.SpeakerToggle -> { /* TODO: speaker on/off */ }
            }
        }
    }
}
