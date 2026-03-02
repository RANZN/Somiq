package com.ranjan.somiq.reels.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.reels.ui.ReelsContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reels",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ReelsScreen(
                uiState = uiState,
                onAction = viewModel::handleAction
            )
        }
    }
}
