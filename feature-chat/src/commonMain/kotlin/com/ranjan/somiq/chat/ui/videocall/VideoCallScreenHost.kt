package com.ranjan.somiq.chat.ui.videocall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VideoCallScreenHost(
    otherUserId: String,
    otherUserName: String,
    onCallEnded: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: VideoCallViewModel = koinViewModel(parameters = { parametersOf(otherUserId, otherUserName) })
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is VideoCallContract.Effect.CallEnded -> onCallEnded()
        }
    }

    VideoCallScreen(uiState = uiState, onIntent = viewModel::handleIntent, modifier = modifier)
}
