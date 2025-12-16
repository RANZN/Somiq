package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.profile.ui.ProfileContract.Action
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenHost(
    userId: String? = null,
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToEditProfile: (String) -> Unit = {},
    onNavigateToSettings: (String) -> Unit = {},
    onNavigateToFollowers: (String) -> Unit = {},
    onNavigateToFollowing: (String) -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()

    // Set userId when screen is first composed
    androidx.compose.runtime.LaunchedEffect(userId) {
        viewModel.setUserId(userId)
        viewModel.handleAction(Action.LoadProfile)
    }

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToEditProfile -> onNavigateToEditProfile(effect.userId)
            is Effect.NavigateToSettings -> onNavigateToSettings(effect.userId)
            is Effect.NavigateToFollowers -> onNavigateToFollowers(effect.userId)
            is Effect.NavigateToFollowing -> onNavigateToFollowing(effect.userId)
        }
    }

    ProfileScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}
