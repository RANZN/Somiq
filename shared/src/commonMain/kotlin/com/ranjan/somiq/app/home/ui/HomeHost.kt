package com.ranjan.somiq.app.home.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.core.presentation.util.collectEffects
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationHost(
    modifier: Modifier = Modifier,
    onNavigateToUser: (String) -> Unit,
    onNavigateToPost: (String) -> Unit,
    onNavigateToComments: (String) -> Unit,
    onNavigateToStory: (String) -> Unit,
    onShowShareDialog: (String) -> Unit,
    onShowMoreOptions: (String) -> Unit,
    onNavigateToEditProfile: (String) -> Unit,
    onNavigateToSettings: (String) -> Unit,
    onNavigateToFollowers: (String) -> Unit,
    onNavigateToFollowing: (String) -> Unit,
    onNavigateToConversation: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToCreatePost: () -> Unit = {},
    onNavigateToCreateStory: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
        when (effect) {
            HomeContract.Effect.Setting -> navigateToSettings()
            is HomeContract.Effect.NavigateToUser -> onNavigateToUser(effect.userId)
            is HomeContract.Effect.NavigateToPost -> onNavigateToPost(effect.postId)
            is HomeContract.Effect.NavigateToComments -> onNavigateToComments(effect.postId)
            is HomeContract.Effect.NavigateToStory -> onNavigateToStory(effect.storyId)
            is HomeContract.Effect.ShowShareDialog -> onShowShareDialog(effect.postId)
            is HomeContract.Effect.ShowMoreOptions -> onShowMoreOptions(effect.postId)
            is HomeContract.Effect.NavigateToEditProfile -> onNavigateToEditProfile(effect.userId)
            is HomeContract.Effect.NavigateToSettings -> onNavigateToSettings(effect.userId)
            is HomeContract.Effect.NavigateToFollowers -> onNavigateToFollowers(effect.userId)
            is HomeContract.Effect.NavigateToFollowing -> onNavigateToFollowing(effect.userId)
            is HomeContract.Effect.NavigateToConversation -> onNavigateToConversation(effect.userId)
            HomeContract.Effect.NavigateToNotifications -> onNavigateToNotifications()
            HomeContract.Effect.NavigateToCreatePost -> onNavigateToCreatePost()
            HomeContract.Effect.NavigateToCreateStory -> onNavigateToCreateStory()
            HomeContract.Effect.NavigateToNewChat -> {}
        }
    }
    HomeScreen(
        state = state,
        action = viewModel::handleIntent,
        modifier = modifier,
    )
}
