package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.ranjan.somiq.app.home.ui.HomeNavigationHost
import com.ranjan.somiq.app.postDetail.ui.PostDetailScreen
import com.ranjan.somiq.chat.ui.conversation.ConversationScreenHost
import com.ranjan.somiq.chat.ui.videocall.VideoCallScreenHost
import com.ranjan.somiq.chat.ui.voicecall.VoiceCallScreenHost
import com.ranjan.somiq.collections.CollectionsScreen
import com.ranjan.somiq.core.di.InitializeCoil
import com.ranjan.somiq.createpost.CreatePostEntry
import com.ranjan.somiq.createstory.CreateStoryEntry
import com.ranjan.somiq.feed.ui.storyview.StoryViewScreenHost
import com.ranjan.somiq.notifications.NotificationsScreen
import com.ranjan.somiq.profile.ui.ProfileScreenHost

fun EntryProviderScope<NavKey>.homeEntries(
    backStack: NavBackStack<NavKey>,
    homeViewModelKey: String,
) {
    entry<HomeGraph> {
        InitializeCoil()
        HomeNavigationHost(
            homeViewModelKey = homeViewModelKey,
            onNavigateToUser = { backStack.add(Home.UserProfile) },
            onNavigateToPost = { postId -> backStack.add(PostDetail(postId)) },
            onNavigateToComments = { postId -> backStack.add(PostDetail(postId)) },
            onNavigateToStory = { storyId -> backStack.add(StoryView(storyId)) },
            onNavigateToHashtag = { },
            onShowShareDialog = { },
            onShowMoreOptions = { },
            onNavigateToEditProfile = { },
            onNavigateToSettings = { },
            onNavigateToFollowers = { },
            onNavigateToFollowing = { },
            onNavigateToConversation = { userId -> backStack.add(Conversation(userId)) },
            onNavigateToNotifications = { backStack.add(Notifications) },
            onNavigateToCreatePost = { backStack.add(CreatePostScreen) },
            onNavigateToCreateStory = { backStack.add(CreateStoryScreen) },
            logout = {
                backStack.clear()
                backStack.add(OnBoarding.Login)
            }
        )
    }

    entry<CreatePostScreen> {
        CreatePostEntry(onBack = { backStack.removeLastOrNull() })
    }

    entry<CreateStoryScreen> {
        CreateStoryEntry(onBack = { backStack.removeLastOrNull() })
    }

    entry<StoryView> { key ->
        StoryViewScreenHost(
            storyId = key.storyId,
            onBack = { backStack.removeLastOrNull() },
        )
    }

    entry<Profile> { key ->
        ProfileScreenWithBack(
            userId = key.userId,
            onBack = { backStack.removeLastOrNull() },
            onNavigateToUser = { uid -> backStack.add(Profile(uid)) },
            onNavigateToPost = { postId -> backStack.add(PostDetail(postId)) },
            onNavigateToEditProfile = { },
            onNavigateToSettings = { },
            onNavigateToFollowers = { },
            onNavigateToFollowing = { },
        )
    }

    entry<Conversation> { key ->
        ConversationScreenHost(
            otherUserId = key.userId,
            otherUserName = "User",
            onStartVoiceCall = { userId -> backStack.add(VoiceCall(userId)) },
            onStartVideoCall = { userId -> backStack.add(VideoCall(userId)) },
        )
    }

    entry<VoiceCall> { key ->
        VoiceCallScreenHost(
            otherUserId = key.userId,
            otherUserName = "User",
            onCallEnded = { backStack.removeLastOrNull() },
        )
    }

    entry<VideoCall> { key ->
        VideoCallScreenHost(
            otherUserId = key.userId,
            otherUserName = "User",
            onCallEnded = { backStack.removeLastOrNull() },
        )
    }

    entry<PostDetail> { key ->
        PostDetailScreen(postId = key.postId)
    }

    entry<Notifications> {
        NotificationsScreen()
    }

    entry<Collections> {
        CollectionsScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenWithBack(
    userId: String?,
    onBack: () -> Unit,
    onNavigateToUser: (String) -> Unit,
    onNavigateToPost: (String) -> Unit,
    onNavigateToEditProfile: (String) -> Unit,
    onNavigateToSettings: (String) -> Unit,
    onNavigateToFollowers: (String) -> Unit,
    onNavigateToFollowing: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ProfileScreenHost(
                userId = userId,
                onNavigateToEditProfile = onNavigateToEditProfile,
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToFollowers = onNavigateToFollowers,
                onNavigateToFollowing = onNavigateToFollowing,
                onNavigateToPost = onNavigateToPost,
            )
        }
    }
}
