package com.ranjan.somiq.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.reels.data.model.Reel
import com.ranjan.somiq.search.data.model.User
import com.ranjan.somiq.search.ui.SearchContract.Action
import com.ranjan.somiq.search.ui.SearchContract.UiState

@Composable
fun SearchScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { onAction(Action.OnQueryChange(it)) },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (uiState.error != null) {
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
                }
            }
        } else if (uiState.hasResults) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val results = uiState.searchResults!!
                
                if (results.users.isNotEmpty()) {
                    item {
                        Text(
                            text = "Users",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(results.users) { user ->
                        UserSearchItem(
                            user = user,
                            onClick = { onAction(Action.OnUserClick(user.id)) }
                        )
                    }
                }
                
                if (results.posts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Posts",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp, 8.dp)
                        )
                    }
                    items(results.posts) { post ->
                        PostSearchItem(
                            post = post,
                            onClick = { onAction(Action.OnPostClick(post.id)) }
                        )
                    }
                }
                
                if (results.reels.isNotEmpty()) {
                    item {
                        Text(
                            text = "Reels",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp, 8.dp)
                        )
                    }
                    items(results.reels) { reel ->
                        ReelSearchItem(
                            reel = reel,
                            onClick = { onAction(Action.OnPostClick(reel.id)) }
                        )
                    }
                }
            }
        } else if (uiState.isSearchActive) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun UserSearchItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (user.username != null) {
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PostSearchItem(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = post.content.take(100) + if (post.content.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ReelSearchItem(
    reel: Reel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = reel.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            val description = reel.description
            if (description != null) {
                Text(
                    text = description.take(100) + if (description.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
