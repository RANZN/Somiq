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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.somiq.app.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.chat.ui.chatlist.ChatListScreenHost
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.feed.ui.FeedScreenHost
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import com.ranjan.somiq.shared.resources.Res
import com.ranjan.somiq.shared.resources.calls
import com.ranjan.somiq.shared.resources.chats
import com.ranjan.somiq.shared.resources.updates
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationHost(
    sessionId: String,
    modifier: Modifier = Modifier,
    onNavigateToUser: (String) -> Unit,
    onNavigateToPost: (String) -> Unit,
    onNavigateToComments: (String) -> Unit,
    onNavigateToStory: (String) -> Unit,
    onShowShareDialog: (String) -> Unit,
    onShowMoreOptions: (String) -> Unit,
    onNavigateToEditProfile: (String) -> Unit,
    onNavigateToSettings: (String) -> Unit,
    onNavigateToFollowers: (String) -> Unit,
    onNavigateToFollowing: (String) -> Unit,
    onNavigateToConversation: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToCreatePost: () -> Unit = {},
    onNavigateToCreateStory: () -> Unit = {},
    logout: () -> Unit = {},
) {
    val viewModel: HomeViewModel = koinViewModel(key = sessionId)
    val state by viewModel.state.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.getIndex(),
        pageCount = { HomeTab.items.size },
    )

    LaunchedEffect(state.selectedTab) {
        val target = state.selectedTab.getIndex()
        if (target >= 0 && target != pagerState.currentPage) {
            pagerState.animateScrollToPage(target)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            val tab = HomeTab.fromIndex(page)
            if (tab != state.selectedTab) {
                viewModel.handleIntent(HomeContract.Intent.SelectTab(tab))
            }
        }
    }

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            HomeContract.Effect.Logout -> logout()
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
                            IconButton(onClick = onNavigateToNotifications) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }

                        HomeTab.Profile -> {
                            IconButton(onClick = { viewModel.handleIntent(HomeContract.Intent.Logout) }) {
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
                onTabSelected = { viewModel.handleIntent(HomeContract.Intent.SelectTab(it)) },
                modifier = Modifier.navigationBarsPadding(),
            )
        },
        floatingActionButton = {
            when (state.selectedTab) {
                HomeTab.Updates -> {
                    FloatingActionButton(onClick = onNavigateToCreatePost) {
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
                        onNavigateToConversation = onNavigateToConversation,
                    )
                }

                HomeTab.Updates -> {
                    FeedScreenHost(
                        viewModelKey = sessionId,
                        scrollToTopTrigger = state.scrollToTopKey,
                        onCreatePost = onNavigateToCreatePost,
                        onNavigateToNotifications = onNavigateToNotifications,
                        onNavigateToCreateStory = onNavigateToCreateStory,
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToPost = onNavigateToPost,
                        onNavigateToComments = onNavigateToComments,
                        onNavigateToStory = onNavigateToStory,
                        onShowShareDialog = onShowShareDialog,
                        onShowMoreOptions = onShowMoreOptions,
                    )
                }

                HomeTab.Profile -> {
                    ProfileScreenHost(
                        viewModelKey = sessionId,
                        scrollToTopTrigger = state.scrollToTopKey,
                        onLogout = { viewModel.handleIntent(HomeContract.Intent.Logout) },
                        onNavigateToEditProfile = onNavigateToEditProfile,
                        onNavigateToSettings = onNavigateToSettings,
                        onNavigateToFollowers = onNavigateToFollowers,
                        onNavigateToFollowing = onNavigateToFollowing,
                        onNavigateToPost = onNavigateToPost,
                    )
                }

                HomeTab.Calls -> {}
            }
        }
    }
}
