package com.ranjan.somiq.chat.ui.videocall

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.core.presentation.util.collectEffects
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
        when (effect) {
            is VideoCallContract.Effect.CallEnded -> onCallEnded()
        }
    }
    VideoCallScreen(uiState = uiState, onIntent = viewModel::handleIntent, modifier = modifier)
}
