package com.ranjan.somiq.feed.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.core.domain.UploadState
import com.ranjan.somiq.core.presentation.component.AppAsyncImage
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun UploadProgressBanner(
    state: UploadState,
    onSuccessFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state is UploadState.Idle) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val imageUri = when (state) {
            is UploadState.Uploading -> state.imageUris.firstOrNull()
            else -> null
        }

        if (imageUri != null) {
            AppAsyncImage(
                imageUrl = imageUri,
                contentDescription = "Uploading post preview",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Spacer(modifier = Modifier.size(24.dp))
        }

        when (state) {
            is UploadState.Uploading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Posting...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            is UploadState.Success -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Posted successfully!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LaunchedEffect(Unit) {
                    delay(2.seconds)
                    onSuccessFinished()
                }
            }
            is UploadState.Failed -> {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Failed",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Failed to post.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                LaunchedEffect(Unit) {
                    delay(4.seconds)
                    onSuccessFinished()
                }
            }
            else -> {}
        }
    }
}
