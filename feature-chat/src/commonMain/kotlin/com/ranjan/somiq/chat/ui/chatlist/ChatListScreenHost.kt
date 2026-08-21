package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.chat.ui.chatlist.ChatListContract.Effect
import com.ranjan.somiq.core.presentation.model.ScreenUiConfig
import com.ranjan.somiq.core.presentation.util.collectEffects
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.chats
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreenHost(
    onConfigureUi: (ScreenUiConfig) -> Unit = {},
    onNavigateToConversation: (String) -> Unit = {},
    onNewChat: () -> Unit = {},
) {
    val viewModel: ChatListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.collectEffects { effect ->
        when (effect) {
            is Effect.NavigateToConversation -> onNavigateToConversation(effect.userId)
        }
    }
    val config = remember {
        ScreenUiConfig(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.chats),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    )
                )
            },
            fab = {
                FloatingActionButton(onClick = onNewChat) {
                    Icon(Icons.Default.Edit, contentDescription = "New message")
                }
            }
        )
    }
    LaunchedEffect(Unit) {
        onConfigureUi(config)
    }
    ChatListScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}
