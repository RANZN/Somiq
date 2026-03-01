package com.ranjan.somiq.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.profile.ui.ProfileContract.Action
import com.ranjan.somiq.profile.ui.ProfileContract.Effect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenHost(
    userId: String? = null,
    appBarTitle: String? = null,
    onLogout: () -> Unit = {},
    onNavigateToEditProfile: (String) -> Unit = {},
    onNavigateToSettings: (String) -> Unit = {},
    onNavigateToFollowers: (String) -> Unit = {},
    onNavigateToFollowing: (String) -> Unit = {}
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
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

    if (appBarTitle != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = appBarTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
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
                ProfileScreen(
                    uiState = uiState,
                    onAction = viewModel::handleAction
                )
            }
        }
    } else {
        ProfileScreen(
            uiState = uiState,
            onAction = viewModel::handleAction
        )
    }
}
