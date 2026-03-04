package com.ranjan.somiq.app.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranjan.somiq.app.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.app.search.ui.SearchScreenHost
import com.ranjan.somiq.core.presentation.navigation.Home
import com.ranjan.somiq.core.presentation.util.CollectEffect
import com.ranjan.somiq.feed.ui.FeedScreenHost
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import com.ranjan.somiq.reels.ui.ReelsScreenHost
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationHost(
    onNavigateToUser: (String) -> Unit,
    onNavigateToPost: (String) -> Unit,
    onNavigateToComments: (String) -> Unit,
    onNavigateToStory: (String) -> Unit,
    onNavigateToHashtag: (String) -> Unit,
    onShowShareDialog: (String) -> Unit,
    onShowMoreOptions: (String) -> Unit,
    onNavigateToEditProfile: (String) -> Unit,
    onNavigateToSettings: (String) -> Unit,
    onNavigateToFollowers: (String) -> Unit,
    onNavigateToFollowing: (String) -> Unit,
    onNavigateToConversation: (String) -> Unit = {},
    onNavigateToLogin: () -> Unit,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToCreatePost: () -> Unit = {},
    onNavigateToCreateStory: () -> Unit = {},
    onNavigateToChatList: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    CollectEffect(viewModel.effect) { effect ->
        when (effect) {
            HomeContract.Effect.NavigateToLogin -> onNavigateToLogin()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTab = state.selectedTab,
                onTabSelected = { viewModel.handleIntent(HomeContract.Intent.SelectTab(it)) },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            when (val selectedTab = state.selectedTab) {
                Home.Feed -> {
                    FeedScreenHost(
                        onCreatePost = onNavigateToCreatePost,
                        onNavigateToNotifications = onNavigateToNotifications,
                        onNavigateToChat = onNavigateToChatList,
                        onNavigateToCreateStory = onNavigateToCreateStory,
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToPost = onNavigateToPost,
                        onNavigateToComments = onNavigateToComments,
                        onNavigateToStory = onNavigateToStory,
                        onShowShareDialog = onShowShareDialog,
                        onShowMoreOptions = onShowMoreOptions
                    )
                }

                Home.Search -> {
                    SearchScreenHost(
                        externalSearchQuery = state.searchQuery,
                        onExternalQueryChange = { viewModel.handleIntent(HomeContract.Intent.SearchQueryChange(it)) },
                        showSearchFieldInContent = false,
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToHashtag = onNavigateToHashtag,
                        onNavigateToPost = onNavigateToPost
                    )
                }

                Home.Reels -> {
                    ReelsScreenHost(
                        onNavigateToReel = { reelId -> /* TODO */ },
                        onNavigateToComments = { reelId -> /* TODO */ },
                        onShowShareDialog = onShowShareDialog
                    )
                }

                is Home.Profile -> {
                    ProfileScreenHost(
                        userId = (selectedTab as Home.Profile).userId,
                        appBarTitle = state.currentUserName,
                        onLogout = { viewModel.handleIntent(HomeContract.Intent.Logout) },
                        onNavigateToEditProfile = onNavigateToEditProfile,
                        onNavigateToSettings = onNavigateToSettings,
                        onNavigateToFollowers = onNavigateToFollowers,
                        onNavigateToFollowing = onNavigateToFollowing
                    )
                }
            }
        }
    }
}
