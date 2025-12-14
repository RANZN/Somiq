package com.ranjan.somiq.feed.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreenHost(
    viewModel: FeedViewModel = koinViewModel(),
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
            is FeedEvent.NavigateToPost -> onNavigateToPost(event.postId)
            is FeedEvent.NavigateToUser -> onNavigateToUser(event.userId)
            is FeedEvent.NavigateToComments -> onNavigateToComments(event.postId)
            is FeedEvent.ShowShareDialog -> onShowShareDialog(event.postId)
            is FeedEvent.ShowMoreOptions -> onShowMoreOptions(event.postId)
            is FeedEvent.NavigateToStory -> onNavigateToStory(event.storyId)
        }
    }

    FeedScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}
