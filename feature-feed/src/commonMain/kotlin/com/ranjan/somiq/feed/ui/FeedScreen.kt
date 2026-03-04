package com.ranjan.somiq.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.feed.ui.FeedContract.Intent
import com.ranjan.somiq.feed.ui.FeedContract.UiState
import com.ranjan.somiq.feed.ui.components.PostItem
import com.ranjan.somiq.feed.ui.components.StoriesSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    uiState: UiState,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SomiQ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(Intent.OnCreatePostClick) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Post",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onIntent(Intent.OnNotificationsClick) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { onIntent(Intent.OnChatClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Chat",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.posts.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                uiState.error != null && uiState.posts.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = uiState.error!!,
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
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            StoriesSection(
                                stories = uiState.stories,
                                modifier = Modifier.fillMaxWidth(),
                                showAddStoryItem = true,
                                onAddStoryClick = { onIntent(Intent.OnAddStoryClick) },
                                onStoryClick = { storyId -> onIntent(Intent.OnStoryClick(storyId)) }
                            )
                        }

                        items(
                            items = uiState.posts,
                            key = { it.id }
                        ) { post ->
                            PostItem(
                                post = post,
                                onLikeClick = { onIntent(Intent.ToggleLike(post.id)) },
                                onCommentClick = { onIntent(Intent.OnCommentClick(post.id)) },
                                onShareClick = { onIntent(Intent.OnShareClick(post.id)) },
                                onSaveClick = { onIntent(Intent.ToggleBookmark(post.id)) },
                                onMoreClick = { onIntent(Intent.OnMoreClick(post.id)) },
                                onUserClick = { onIntent(Intent.OnUserClick(post.authorId)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
