package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.core.presentation.util.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenHost(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToEditProfile: (String) -> Unit = {},
    onNavigateToSettings: (String) -> Unit = {},
    onNavigateToFollowers: (String) -> Unit = {},
    onNavigateToFollowing: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            is ProfileEvent.NavigateToEditProfile -> onNavigateToEditProfile(event.userId)
            is ProfileEvent.NavigateToSettings -> onNavigateToSettings(event.userId)
            is ProfileEvent.NavigateToFollowers -> onNavigateToFollowers(event.userId)
            is ProfileEvent.NavigateToFollowing -> onNavigateToFollowing(event.userId)
        }
    }

    ProfileScreen(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}

