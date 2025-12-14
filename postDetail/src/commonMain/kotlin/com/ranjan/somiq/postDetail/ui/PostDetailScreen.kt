package com.ranjan.somiq.postDetail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: PostDetailViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleAction(PostDetailAction.LoadPost)
        viewModel.handleAction(PostDetailAction.LoadComments)
    }

    LaunchedEffect(events) {
        events?.let { event ->
            when (event) {
                is PostDetailEvent.ShowError -> {
                    // Handle error (could show snackbar)
                }
                is PostDetailEvent.CommentPosted -> {
                    // Handle success
                }
            }
            viewModel.clearEvent()
        }
    }

    when {
        uiState.isLoading && uiState.post == null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null && uiState.post == null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val errorText = uiState.error
                    if (errorText != null) {
                        Text(
                            text = errorText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.handleAction(PostDetailAction.Refresh) }) {
                        Text("Retry")
                    }
                }
            }
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Post content
                uiState.post?.let { post ->
                    item {
                        Column {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "❤️ ${post.likesCount}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "💬 ${uiState.comments.size}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // Comment input
                item {
                    OutlinedTextField(
                        value = uiState.commentText,
                        onValueChange = { viewModel.handleAction(PostDetailAction.UpdateCommentText(it)) },
                        label = { Text("Add a comment...") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.handleAction(PostDetailAction.PostComment) },
                                enabled = uiState.commentText.isNotBlank()
                            ) {
                                Text("Post")
                            }
                        }
                    )
                }

                // Comments section
                item {
                    Text(
                        text = "Comments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (uiState.isLoadingComments) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth().padding(16.dp))
                    }
                } else {
                    items(uiState.comments) { comment ->
                        CommentItem(
                            comment = comment,
                            onLikeClick = { viewModel.handleAction(PostDetailAction.ToggleCommentLike(comment.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: com.ranjan.somiq.postDetail.data.model.CommentResponse,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = comment.authorName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                comment.authorUsername?.let {
                    Text(
                        text = "@$it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = onLikeClick) {
                    Text(
                        text = "❤️ ${comment.likesCount}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (comment.repliesCount > 0) {
                    Text(
                        text = "${comment.repliesCount} replies",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
