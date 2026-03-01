package com.ranjan.somiq.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.feed.ui.FeedContract.Action
import com.ranjan.somiq.feed.ui.FeedContract.UiState
import com.ranjan.somiq.feed.ui.components.PostItem
import com.ranjan.somiq.feed.ui.components.StoriesSection

@Composable
fun FeedScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        if (uiState.isLoading && uiState.posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else if (uiState.error != null && uiState.posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Pull to refresh",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.stories.isNotEmpty()) {
                    item {
                        StoriesSection(
                            stories = uiState.stories,
                            modifier = Modifier.fillMaxWidth(),
                            onStoryClick = { storyId -> onAction(Action.OnStoryClick(storyId)) }
                        )
                    }
                }

                items(
                    items = uiState.posts,
                    key = { it.id }
                ) { post ->
                    PostItem(
                        post = post,
                        onLikeClick = { onAction(Action.ToggleLike(post.id)) },
                        onCommentClick = { onAction(Action.OnCommentClick(post.id)) },
                        onShareClick = { onAction(Action.OnShareClick(post.id)) },
                        onSaveClick = { onAction(Action.ToggleBookmark(post.id)) },
                        onMoreClick = { onAction(Action.OnMoreClick(post.id)) },
                        onUserClick = { onAction(Action.OnUserClick(post.authorId)) }
                    )
                }
            }
        }
    }
}
