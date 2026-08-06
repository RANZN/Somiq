package com.ranjan.somiq.app.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.ranjan.somiq.app.home.ui.HomeContract.Intent
import com.ranjan.somiq.app.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.chat.ui.chatlist.ChatListScreenHost
import com.ranjan.somiq.core.presentation.model.ScreenUiConfig
import com.ranjan.somiq.core.presentation.snackbar.LocalSnackbar
import com.ranjan.somiq.feed.ui.FeedScreenHost
import com.ranjan.somiq.profile.ui.ProfileScreenHost

@Composable
fun HomeScreen(
    state: HomeContract.UiState,
    action: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = LocalSnackbar.current
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
            action(Intent.SelectTab(tab))
        }
    }

    val uiConfigs = remember { mutableStateMapOf<HomeTab, ScreenUiConfig>() }
    val currentConfig = uiConfigs[state.selectedTab]

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            currentConfig?.topBar?.invoke()
        },
        floatingActionButton = {
            AnimatedVisibility(currentConfig?.fab != null) {
                currentConfig?.fab?.invoke()
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentTab = { state.selectedTab },
                onTabSelected = { action(Intent.SelectTab(it)) },
                modifier = Modifier.navigationBarsPadding(),
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
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
                        onConfigureUi = {
                            uiConfigs[HomeTab.ChatLists] = it
                        },
                        onNavigateToConversation = {
                            action(Intent.NavigateToConversation(it))
                        },
                        onNewChat = { action(Intent.NewChat) }
                    )
                }

                HomeTab.Updates -> {
                    FeedScreenHost(
                        onConfigureUi = {
                            uiConfigs[HomeTab.Updates] = it
                        },
                        scrollToTopTrigger = state.scrollToTopKey,
                        onCreatePost = { action(Intent.CreatePost) },
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
                        onConfigureUi = {
                            uiConfigs[HomeTab.Profile] = it
                        },
                        scrollToTopTrigger = state.scrollToTopKey,
                        onNavigateToEditProfile = {
                            action(Intent.NavigateToEditProfile(it))
                        },
                        onNavigateToSettings = { action(Intent.NavigateToSettings(it)) },
                        onNavigateToFollowers = { action(Intent.NavigateToFollowers(it)) },
                        onNavigateToFollowing = { action(Intent.NavigateToFollowing(it)) },
                        onNavigateToPost = { action(Intent.NavigateToPost(it)) },
                        navigateToSettings = { action(Intent.Setting) },
                    )
                }

                HomeTab.Calls -> {}
            }
        }
    }
}
