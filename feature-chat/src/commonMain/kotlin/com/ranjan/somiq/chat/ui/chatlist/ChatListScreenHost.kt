package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ranjan.somiq.chat.ui.chatlist.ChatListContract.Effect
import com.ranjan.somiq.core.presentation.util.CollectEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListScreenHost(
    onNavigateToConversation: (String) -> Unit = {}
) {
    val viewModel: ChatListViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()

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
