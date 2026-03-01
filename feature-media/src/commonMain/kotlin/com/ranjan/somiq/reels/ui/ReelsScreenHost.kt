package com.ranjan.somiq.reels.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.reels.ui.ReelsContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReelsScreenHost(
    viewModel: ReelsViewModel = koinViewModel(),
    onNavigateToReel: (String) -> Unit = {},
    onNavigateToComments: (String) -> Unit = {},
    onShowShareDialog: (String) -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToReel -> onNavigateToReel(effect.reelId)
            is Effect.NavigateToComments -> onNavigateToComments(effect.reelId)
            is Effect.ShowShareDialog -> onShowShareDialog(effect.reelId)
        }
    }

    ReelsScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}
