package com.ranjan.somiq.chat.ui.videocall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.chat.ui.videocall.VideoCallContract.Action
import com.ranjan.somiq.chat.ui.videocall.VideoCallContract.UiState

@Composable
fun VideoCallScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.height(80.dp))
                Text(uiState.otherUserName, style = MaterialTheme.typography.headlineMedium)
                Text("Video call", style = MaterialTheme.typography.bodyMedium)
                when {
                    uiState.isConnecting -> CircularProgressIndicator(Modifier.padding(16.dp))
                    uiState.isActive -> Text("Video call in progress...", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.isActive) {
                    IconButton(onClick = { onAction(Action.ToggleCamera) }) {
                        Icon(
                            if (uiState.isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                            contentDescription = if (uiState.isCameraOn) "Turn off camera" else "Turn on camera"
                        )
                    }
                }
                if (!uiState.isActive && !uiState.isConnecting) {
                    IconButton(onClick = { onAction(Action.StartCall) }) {
                        Icon(Icons.Default.Call, contentDescription = "Start video call")
                    }
                }
                IconButton(onClick = { onAction(Action.EndCall) }) {
                    Icon(Icons.Default.CallEnd, contentDescription = "End call", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
