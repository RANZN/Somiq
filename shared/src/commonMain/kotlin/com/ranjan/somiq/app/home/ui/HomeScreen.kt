package com.ranjan.somiq.app.home.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ranjan.somiq.app.home.ui.HomeContract.Intent
import com.ranjan.somiq.app.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.chat.ui.chatlist.ChatListScreenHost
import com.ranjan.somiq.feed.ui.FeedScreenHost
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import com.ranjan.somiq.shared.resources.Res
import com.ranjan.somiq.shared.resources.calls
import com.ranjan.somiq.shared.resources.chats
import com.ranjan.somiq.shared.resources.updates
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    sessionId: String,
    state: HomeContract.UiState,
    action: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.getIndex(),
        pageCount = { HomeTab.items.size }
    )

    LaunchedEffect(state.selectedTab) {
        val target = state.selectedTab.getIndex()
        if (target >= 0 && (target != pagerState.currentPage)) {
            pagerState.animateScrollToPage(target)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            val tab = HomeTab.fromIndex(page)
            if (tab != state.selectedTab) {
                action(Intent.SelectTab(tab))
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    val title = when (state.selectedTab) {
                        HomeTab.Profile -> state.currentUserName
                        HomeTab.Calls -> stringResource(Res.string.calls)
                        HomeTab.ChatLists -> stringResource(Res.string.chats)
                        HomeTab.Updates -> stringResource(Res.string.updates)
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                actions = {
                    when (state.selectedTab) {
                        HomeTab.Updates -> {
                            IconButton(onClick = { action(Intent.NavigateToNotifications) }) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }

                        HomeTab.Profile -> {
                            IconButton(onClick = { action(Intent.Logout) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }

                        else -> Unit
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentTab = { state.selectedTab },
                onTabSelected = { action(Intent.SelectTab(it)) },
                modifier = Modifier.navigationBarsPadding(),
            )
        },
        floatingActionButton = {
            when (state.selectedTab) {
                HomeTab.Updates -> {
                    FloatingActionButton(onClick = { action(Intent.NavigateToCreatePost) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create post",
                        )
                    }
                }

                else -> Unit
            }
        },
    ) { padding ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = pagerState,
            beyondViewportPageCount = HomeTab.items.lastIndex,
        ) { page ->
            when (HomeTab.fromIndex(page)) {
                HomeTab.ChatLists -> {
                    ChatListScreenHost(
                        viewModelKey = sessionId,
                        onNavigateToConversation = {
                            action(Intent.NavigateToConversation(it))
                        },
                    )
                }

                HomeTab.Updates -> {
                    FeedScreenHost(
                        viewModelKey = sessionId,
                        scrollToTopTrigger = state.scrollToTopKey,
                        onCreatePost = { action(Intent.NavigateToCreatePost) },
                        onNavigateToNotifications = { action(Intent.NavigateToNotifications) },
                        onNavigateToCreateStory = { action(Intent.NavigateToCreateStory) },
                        onNavigateToUser = { action(Intent.NavigateToUser(it)) },
                        onNavigateToPost = { action(Intent.NavigateToPost(it)) },
                        onNavigateToComments = { action(Intent.NavigateToComments(it)) },
                        onNavigateToStory = { action(Intent.NavigateToStory(it)) },
                        onShowShareDialog = { action(Intent.ShowShareDialog(it)) },
                        onShowMoreOptions = { action(Intent.ShowMoreOptions(it)) },
                    )
                }

                HomeTab.Profile -> {
                    ProfileScreenHost(
                        viewModelKey = sessionId,
                        scrollToTopTrigger = state.scrollToTopKey,
                        onLogout = { action(Intent.Logout) },
                        onNavigateToEditProfile = {
                            action(Intent.NavigateToEditProfile(it))
                        },
                        onNavigateToSettings = { action(Intent.NavigateToSettings(it)) },
                        onNavigateToFollowers = { action(Intent.NavigateToFollowers(it)) },
                        onNavigateToFollowing = { action(Intent.NavigateToFollowing(it)) },
                        onNavigateToPost = { action(Intent.NavigateToPost(it)) },
                    )
                }

                HomeTab.Calls -> {}
            }
        }
    }
}
