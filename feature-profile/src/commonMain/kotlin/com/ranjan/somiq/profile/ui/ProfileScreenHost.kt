package com.ranjan.somiq.profile.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.ranjan.somiq.core.presentation.model.ScreenUiConfig
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import com.ranjan.somiq.profile.ui.ProfileContract.Intent
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenHost(
    onConfigureUi: (ScreenUiConfig) -> Unit = {},
    scrollToTopTrigger: Int = 0,
    userId: String? = null,
    onNavigateToEditProfile: (String) -> Unit = {},
    onNavigateToSettings: (String) -> Unit = {},
    onNavigateToFollowers: (String) -> Unit = {},
    onNavigateToFollowing: (String) -> Unit = {},
    onNavigateToPost: (String) -> Unit = {},
    navigateToSettings: () -> Unit = {},
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        viewModel.handleIntent(Intent.LoadProfile(userId))
    }

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToEditProfile -> onNavigateToEditProfile(effect.userId)
            is Effect.NavigateToSettings -> navigateToSettings()
            is Effect.NavigateToFollowers -> onNavigateToFollowers(effect.userId)
            is Effect.NavigateToFollowing -> onNavigateToFollowing(effect.userId)
        }
    }

    val config = remember(uiState.profile) {
        ScreenUiConfig(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = uiState.profile?.user?.username.orEmpty()
                        )
                    },
                    actions = {
                        IconButton(onClick = { viewModel.handleIntent(Intent.Setting) }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                )
            }
        )
    }

    LaunchedEffect(uiState.profile) {
        onConfigureUi(config)
    }

    ProfileScreen(
        uiState = uiState,
        onPostClick = onNavigateToPost,
        scrollToTopTrigger = scrollToTopTrigger,
        onIntent = viewModel::handleIntent,
    )
}
