package com.ranjan.somiq.feed.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.feed.ui.FeedContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreenHost(
    onCreatePost: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onNavigateToCreateStory: () -> Unit = {},
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {},
    onNavigateToComments: (String) -> Unit = {},
    onNavigateToStory: (String) -> Unit = {},
    onShowShareDialog: (String) -> Unit = {},
    onShowMoreOptions: (String) -> Unit = {}
) {
    val viewModel: FeedViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToPost -> onNavigateToPost(effect.postId)
            is Effect.NavigateToUser -> onNavigateToUser(effect.userId)
            is Effect.NavigateToComments -> onNavigateToComments(effect.postId)
            is Effect.ShowShareDialog -> onShowShareDialog(effect.postId)
            is Effect.ShowMoreOptions -> onShowMoreOptions(effect.postId)
            is Effect.NavigateToStory -> onNavigateToStory(effect.storyId)
            Effect.NavigateToCreatePost -> onCreatePost()
            Effect.NavigateToNotifications -> onNavigateToNotifications()
            Effect.NavigateToChat -> onNavigateToChat()
            Effect.NavigateToCreateStory -> onNavigateToCreateStory()
        }
    }

    FeedScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}
