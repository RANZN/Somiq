package com.ranjan.somiq.app.postDetail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.core.presentation.error.asString
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Intent
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.UiState
import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.ui.components.PostItem
import com.ranjan.somiq.core.util.toTimeAgo
import androidx.compose.ui.tooling.preview.Preview
import com.ranjan.somiq.app.postDetail.data.model.CommentResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    uiState: UiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onIntent: (Intent) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Post Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.post == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null && uiState.post == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val errorRes = uiState.error
                            Text(
                                text = errorRes.asString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onIntent(Intent.Refresh) }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                uiState.post != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Post content
                        item(key = "post") {
                            val post = uiState.post
                            PostItem(
                                post = post,
                                onLikeClick = { onIntent(Intent.ToggleLike) },
                                onSaveClick = { onIntent(Intent.ToggleBookmark) }
                            )
                        }

                        // Comment input
                        item {
                            OutlinedTextField(
                                value = uiState.commentText,
                                onValueChange = { onIntent(Intent.UpdateCommentText(it)) },
                                label = { Text("Add a comment...") },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                trailingIcon = {
                                    IconButton(
                                        onClick = { onIntent(Intent.PostComment) },
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
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        if (uiState.isLoadingComments) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                                )
                            }
                        } else {
                            items(uiState.comments) { comment ->
                                CommentItem(
                                    comment = comment,
                                    onLikeClick = { onIntent(Intent.ToggleCommentLike(comment.id)) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: CommentResponse,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = comment.createdAt.toTimeAgo(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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


@Composable
@Preview
fun PostDetailScreenPreview() {
    MaterialTheme {
        PostDetailScreen(
            uiState = UiState(
                post = Post(
                    id = "1",
                    caption = "This is a preview post caption showing the amazing details!",
                    authorId = "user1",
                    authorName = "John Doe",
                    authorUsername = "johndoe",
                    authorProfilePictureUrl = null,
                    createdAt = 1625097600000L,
                    updatedAt = null,
                    mediaUrls = emptyList(),
                    likesCount = 42,
                    bookmarksCount = 5,
                    isLiked = true,
                    isBookmarked = false
                ),
                comments = listOf(
                    CommentResponse(
                        id = "c1",
                        content = "Wow, this looks incredible! Thanks for sharing.",
                        authorId = "user2",
                        authorName = "Jane Smith",
                        authorUsername = "janesmith",
                        authorProfilePictureUrl = null,
                        createdAt = 1625098600000L,
                        updatedAt = null,
                        likesCount = 3,
                        repliesCount = 0
                    )
                )
            ),
            onBackClick = {},
            onIntent = {}
        )
    }
}
