package com.ranjan.somiq.chat.ui.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.chat.data.model.Conversation
import com.ranjan.somiq.chat.ui.chatlist.ChatListContract.Action
import com.ranjan.somiq.chat.ui.chatlist.ChatListContract.UiState

@Composable
fun ChatListScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.conversations.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.hasError && uiState.conversations.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(uiState.error ?: "Error", color = MaterialTheme.colorScheme.error)
                        Text("Tap to retry", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.clickable { onAction(Action.Retry) })
                    }
                }
            }
            uiState.isEmpty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Chat, contentDescription = null)
                        Text("No conversations yet", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.conversations, key = { it.otherUserId }) { conversation ->
                        ConversationItem(
                            conversation = conversation,
                            onClick = { onAction(Action.OnConversationClick(conversation.otherUserId)) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(conversation.otherUserName, style = MaterialTheme.typography.titleMedium) },
        supportingContent = { Text(conversation.lastMessage ?: "", style = MaterialTheme.typography.bodySmall, maxLines = 1) },
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
