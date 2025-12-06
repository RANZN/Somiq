package com.ranjan.somiq.reels.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReelsScreenHost(
    viewModel: ReelsViewModel = koinViewModel(),
    onNavigateToReel: (String) -> Unit = {},
    onNavigateToComments: (String) -> Unit = {},
    onShowShareDialog: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            is ReelsEvent.NavigateToReel -> onNavigateToReel(event.reelId)
            is ReelsEvent.NavigateToComments -> onNavigateToComments(event.reelId)
            is ReelsEvent.ShowShareDialog -> onShowShareDialog(event.reelId)
        }
    }

    ReelsScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}

