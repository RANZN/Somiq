package com.ranjan.somiq.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.core.presentation.navigation.Screen
import com.ranjan.somiq.feed.ui.FeedScreenHost
import com.ranjan.somiq.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import com.ranjan.somiq.reels.ui.ReelsScreenHost
import com.ranjan.somiq.search.ui.SearchScreenHost
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationHost(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
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
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authRepository: AuthRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()

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
                currentRoute = currentRoute,
                onNavigate = onNavigate,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            when {
                currentRoute?.contains("Feed", ignoreCase = true) == true -> {
                    FeedScreenHost(
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToPost = onNavigateToPost,
                        onNavigateToComments = onNavigateToComments,
                        onNavigateToStory = onNavigateToStory,
                        onShowShareDialog = onShowShareDialog,
                        onShowMoreOptions = onShowMoreOptions
                    )
                }
                currentRoute?.contains("Search", ignoreCase = true) == true -> {
                    SearchScreenHost(
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToHashtag = onNavigateToHashtag,
                        onNavigateToPost = onNavigateToPost
                    )
                }
                currentRoute?.contains("CreatePost", ignoreCase = true) == true -> {
                   /* CreatePostScreenHost(
                        onNavigateBack = { onNavigate(Screen.Home.Feed) },
                        onPostCreated = { onNavigate(Screen.Home.Feed) }
                    )*/
                }
                currentRoute?.contains("Reels", ignoreCase = true) == true -> {
                    ReelsScreenHost(
                        onNavigateToReel = { reelId ->
                            // TODO: Navigate to reel detail
                        },
                        onNavigateToComments = { reelId ->
                            // TODO: Navigate to comments
                        },
                        onShowShareDialog = onShowShareDialog
                    )
                }
                currentRoute?.contains("Profile", ignoreCase = true) == true -> {
                    val userId = try {
                        val route = currentRoute ?: ""
                        val queryIndex = route.indexOf('?')
                        if (queryIndex >= 0) {
                            val query = route.substring(queryIndex + 1)
                            query.split("&").find { it.startsWith("userId=") }?.substringAfter("=")
                        } else {
                            val parts = route.split("/")
                            parts.lastOrNull()?.takeIf { it != "Profile" && it.isNotEmpty() }
                        }
                    } catch (e: Exception) {
                        null
                    }
                    ProfileScreenHost(
                        userId = userId,
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
