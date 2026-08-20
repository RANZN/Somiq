package com.ranjan.somiq.feed.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import com.ranjan.somiq.core.presentation.model.ScreenUiConfig
import com.ranjan.somiq.core.presentation.util.collectEffects
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.updates
import com.ranjan.somiq.feed.ui.FeedContract.Effect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreenHost(
    onConfigureUi: (ScreenUiConfig) -> Unit = {},
    scrollToTopTrigger: Int = 0,
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
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
    val config = remember {
        ScreenUiConfig(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.updates),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    actions = {
                        IconButton(onClick = onNavigateToNotifications) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                )
            },
            fab = {
                FloatingActionButton(
                    onClick = onCreatePost
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create post")
                }
            }
        )
    }
    LaunchedEffect(Unit) {
        onConfigureUi(config)
    }
    FeedScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        scrollToTopTrigger = scrollToTopTrigger
    )
}
