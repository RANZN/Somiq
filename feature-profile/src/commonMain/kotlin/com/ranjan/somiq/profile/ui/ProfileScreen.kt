package com.ranjan.somiq.profile.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.core.presentation.component.AppAsyncImage
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.profile.ui.ProfileContract.Intent
import com.ranjan.somiq.profile.ui.ProfileContract.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    uiState: UiState,
    onIntent: (Intent) -> Unit,
    onPostClick: (String) -> Unit = {},
    scrollToTopTrigger: Int = 0,
    modifier: Modifier = Modifier
) {
    val storiesRowState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            if (uiState.isOwnProfile && uiState.selectedTab == ProfileTab.MyStories) {
                storiesRowState.animateScrollToItem(0)
            } else {
                gridState.animateScrollToItem(0)
            }
        }
    }
    Scaffold(
        topBar = when {
            uiState.showAppBar && uiState.appBarTitle != null -> {
                {
                    TopAppBar(
                        title = {
                            Text(
                                text = uiState.appBarTitle,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        actions = {
                            IconButton(onClick = { onIntent(Intent.OnLogoutClick) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Logout",
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
            }

            else -> {
                {}
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.profile == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                uiState.error != null && uiState.profile == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { onIntent(Intent.Retry) }
                        ) {
                            Text(
                                text = uiState.error,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Tap to retry",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                uiState.profile != null -> {
                    val profile = uiState.profile
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (profile.user.profilePictureUrl != null) {
                                AppAsyncImage(
                                    imageUrl = profile.user.profilePictureUrl,
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profile.user.name.take(1).uppercase(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        Text(
                            text = profile.user.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        if (profile.user.username != null) {
                            Text(
                                text = "@${profile.user.username}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        profile.user.bio?.let { bio ->
                            Text(
                                text = bio,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStat("${profile.postsCount}", "posts")
                            ProfileStat("${profile.followersCount}", "followers")
                            ProfileStat("${profile.followingCount}", "following")
                        }

                        if (uiState.isOwnProfile) {
                            val selectedTabIndex = when (uiState.selectedTab) {
                                ProfileTab.MyStories -> 0
                                ProfileTab.Saved -> 1
                            }
                            SecondaryTabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.padding(top = 24.dp),
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ) {
                                Tab(
                                    selected = uiState.selectedTab == ProfileTab.MyStories,
                                    onClick = { onIntent(Intent.SelectTab(ProfileTab.MyStories)) },
                                    text = { Text("My Stories") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.PhotoCamera,
                                            contentDescription = "My Stories"
                                        )
                                    }
                                )
                                Tab(
                                    selected = uiState.selectedTab == ProfileTab.Saved,
                                    onClick = { onIntent(Intent.SelectTab(ProfileTab.Saved)) },
                                    text = { Text("Saved") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.BookmarkBorder,
                                            contentDescription = "Saved"
                                        )
                                    }
                                )
                            }

                            when (uiState.selectedTab) {
                                ProfileTab.MyStories -> {
                                    if (uiState.myStories.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "No stories yet",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    } else {
                                        LazyRow(
                                            state = storiesRowState,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(top = 8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            contentPadding = PaddingValues(
                                                horizontal = 8.dp,
                                                vertical = 8.dp
                                            )
                                        ) {
                                            items(
                                                items = uiState.myStories,
                                                key = { it.id }
                                            ) { story ->
                                                StoryThumbnail(story = story)
                                            }
                                        }
                                    }
                                }

                                ProfileTab.Saved -> {
                                    LazyVerticalGrid(
                                        state = gridState,
                                        columns = GridCells.Fixed(3),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(top = 8.dp),
                                        contentPadding = PaddingValues(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        items(
                                            items = uiState.savedPosts,
                                            key = { it.id }
                                        ) { post ->
                                            PostGridThumbnail(
                                                post = post,
                                                onClick = { onPostClick(post.id) }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            val posts = uiState.myPosts
                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(3),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(top = 24.dp),
                                contentPadding = PaddingValues(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                items(items = posts, key = { it.id }) { post ->
                                    PostGridThumbnail(
                                        post = post,
                                        onClick = { onPostClick(post.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StoryThumbnail(
    story: Story,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
    ) {
        AppAsyncImage(
            imageUrl = story.mediaUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun PostGridThumbnail(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    ) {
        if (post.mediaUrls.isNotEmpty()) {
            AppAsyncImage(
                imageUrl = post.mediaUrls.first(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}
