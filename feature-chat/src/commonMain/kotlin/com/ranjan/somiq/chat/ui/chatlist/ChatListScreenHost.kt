package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.chat.ui.chatlist.ChatListContract.Effect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListScreenHost(
    onNavigateToConversation: (String) -> Unit = {}
) {
    val viewModel: ChatListViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            is Effect.NavigateToConversation -> onNavigateToConversation(effect.userId)
        }
    }

    ChatListScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}
