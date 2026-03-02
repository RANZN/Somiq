package com.ranjan.somiq.chat.ui.voicecall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.chat.ui.voicecall.VoiceCallContract.Action
import com.ranjan.somiq.chat.ui.voicecall.VoiceCallContract.UiState

@Composable
fun VoiceCallScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.height(80.dp))
        Spacer(Modifier.height(16.dp))
        Text(uiState.otherUserName, style = MaterialTheme.typography.headlineMedium)
        Text("Voice call", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))
        when {
            uiState.isConnecting -> CircularProgressIndicator()
            uiState.isActive -> Text("Call in progress...", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(48.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!uiState.isActive && !uiState.isConnecting) {
                IconButton(onClick = { onAction(Action.StartCall) }) {
                    Icon(Icons.Default.Call, contentDescription = "Start call")
                }
            }
            IconButton(onClick = { onAction(Action.EndCall) }) {
                Icon(Icons.Default.CallEnd, contentDescription = "End call", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
