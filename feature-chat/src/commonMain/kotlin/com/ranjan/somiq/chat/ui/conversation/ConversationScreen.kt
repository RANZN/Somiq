package com.ranjan.somiq.chat.ui.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.chat.data.model.Message
import com.ranjan.somiq.chat.ui.conversation.ConversationContract.Intent
import com.ranjan.somiq.chat.ui.conversation.ConversationContract.UiState

@Composable
fun ConversationScreen(
    uiState: UiState,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier
) {
        Column(modifier = modifier.fillMaxSize()) {
        when {
            uiState.otherUserId.isNotBlank() && !uiState.isLoading -> {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onIntent(Intent.StartVoiceCall) }) {
                        Icon(Icons.Default.Call, contentDescription = "Voice call")
                    }
                    IconButton(onClick = { onIntent(Intent.StartVideoCall) }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video call")
                    }
                }
            }
        }
        when {
            uiState.isLoading && uiState.messages.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    reverseLayout = true,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.messages.reversed(), key = { it.id }) { message ->
                        MessageBubble(
                            message = message,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        value = uiState.messageText,
                        onValueChange = { onIntent(Intent.MessageTextChange(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Message") },
                        singleLine = false,
                        maxLines = 4
                    )
                    androidx.compose.material3.Button(
                        onClick = { onIntent(Intent.SendMessage) },
                        enabled = uiState.messageText.isNotBlank() && !uiState.sending
                    ) {
                        when {
                            uiState.sending -> CircularProgressIndicator(Modifier.padding(4.dp))
                            else -> Text("Send", modifier = Modifier.padding(horizontal = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.isFromMe) androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
                    else androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(message.content, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}
