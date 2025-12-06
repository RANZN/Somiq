package com.ranjan.somiq.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenHost(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {},
    onNavigateToComments: (String) -> Unit = {},
    onNavigateToStory: (String) -> Unit = {},
    onShowShareDialog: (String) -> Unit = {},
    onShowMoreOptions: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            is HomeEvent.NavigateToPost -> onNavigateToPost(event.postId)
            is HomeEvent.NavigateToUser -> onNavigateToUser(event.userId)
            is HomeEvent.NavigateToComments -> onNavigateToComments(event.postId)
            is HomeEvent.ShowShareDialog -> onShowShareDialog(event.postId)
            is HomeEvent.ShowMoreOptions -> onShowMoreOptions(event.postId)
            is HomeEvent.NavigateToStory -> onNavigateToStory(event.storyId)
        }
    }

    HomeScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}

