package com.ranjan.somiq.chat.ui.voicecall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VoiceCallScreenHost(
    otherUserId: String,
    otherUserName: String,
    viewModel: VoiceCallViewModel = koinViewModel(parameters = { parametersOf(otherUserId, otherUserName) }),
    onCallEnded: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is VoiceCallContract.Effect.CallEnded -> onCallEnded()
        }
    }

    VoiceCallScreen(uiState = uiState, onAction = viewModel::handleAction, modifier = modifier)
}
