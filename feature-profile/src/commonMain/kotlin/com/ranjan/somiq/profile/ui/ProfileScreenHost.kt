package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import com.ranjan.somiq.profile.ui.ProfileContract.Intent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenHost(
    scrollToTopTrigger: Int = 0,
    userId: String? = null,
    appBarTitle: String? = null,
    onLogout: () -> Unit = {},
    onNavigateToEditProfile: (String) -> Unit = {},
    onNavigateToSettings: (String) -> Unit = {},
    onNavigateToFollowers: (String) -> Unit = {},
    onNavigateToFollowing: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {}
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
        viewModel.handleIntent(Intent.LoadProfile)
    }

    LaunchedEffect(appBarTitle) {
        viewModel.handleIntent(Intent.SetAppBarConfig(appBarTitle != null, appBarTitle))
    }

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToEditProfile -> onNavigateToEditProfile(effect.userId)
            is Effect.NavigateToSettings -> onNavigateToSettings(effect.userId)
            is Effect.NavigateToFollowers -> onNavigateToFollowers(effect.userId)
            is Effect.NavigateToFollowing -> onNavigateToFollowing(effect.userId)
            Effect.Logout -> onLogout()
        }
    }

    ProfileScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onPostClick = onNavigateToPost,
        scrollToTopTrigger = scrollToTopTrigger
    )
}
