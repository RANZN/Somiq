package com.ranjan.somiq.app.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import com.ranjan.somiq.app.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.chat.ui.chatlist.ChatListScreenHost
import com.ranjan.somiq.core.presentation.navigation.Home
import com.ranjan.somiq.feed.ui.FeedScreenHost
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import com.ranjan.somiq.reels.ui.ReelsScreenHost
import com.ranjan.somiq.app.search.ui.SearchScreenHost
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationHost(
    currentKey: NavKey?,
    onNavigate: (NavKey) -> Unit,
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
    modifier: Modifier = Modifier
) {
    val authRepository: AuthRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()
    val homeKey = currentKey as? Home

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
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            authRepository.logoutUser()
                            onNavigateToLogin()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
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
        },
        bottomBar = {
            BottomNavigationBar(
                currentKey = currentKey,
                onNavigate = onNavigate,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            when (homeKey) {
                Home.Feed -> {
                    FeedScreenHost(
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
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToHashtag = onNavigateToHashtag,
                        onNavigateToPost = onNavigateToPost
                    )
                }
                Home.CreatePost -> {
                    /* CreatePostScreenHost(
                        onNavigateBack = { onNavigate(Home.Feed) },
                        onPostCreated = { onNavigate(Home.Feed) }
                    ) */
                }
                Home.Reels -> {
                    ReelsScreenHost(
                        onNavigateToReel = { reelId -> /* TODO */ },
                        onNavigateToComments = { reelId -> /* TODO */ },
                        onShowShareDialog = onShowShareDialog
                    )
                }
                Home.Chat -> {
                    ChatListScreenHost(onNavigateToConversation = onNavigateToConversation)
                }
                is Home.Profile -> {
                    ProfileScreenHost(
                        userId = homeKey.userId,
                        onNavigateToEditProfile = onNavigateToEditProfile,
                        onNavigateToSettings = onNavigateToSettings,
                        onNavigateToFollowers = onNavigateToFollowers,
                        onNavigateToFollowing = onNavigateToFollowing
                    )
                }
                null -> { /* Not in Home tab */ }
            }
        }
    }
}
