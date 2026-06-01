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
import com.ranjan.somiq.navigation.AppNavGraph.*
import com.ranjan.somiq.notifications.NotificationsScreen
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val homeNavigationModule = module {
    navigation<HomeGraph> {
        val backStack = LocalNavBackStack.current
        InitializeCoil()
        HomeNavigationHost(
            onNavigateToUser = { backStack.add(Profile(it)) },
            onNavigateToPost = { postId -> backStack.add(PostDetail(postId)) },
            onNavigateToComments = { postId -> backStack.add(PostDetail(postId)) },
            onNavigateToStory = { storyId -> backStack.add(StoryView(storyId)) },
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
            navigateToSettings = { backStack.add(Settings) }
        )
    }

    navigation<CreatePostScreen> {
        val backStack = LocalNavBackStack.current
        CreatePostEntry(onBack = { backStack.removeLastOrNull() })
    }

    navigation<CreateStoryScreen> {
        val backStack = LocalNavBackStack.current
        CreateStoryEntry(onBack = { backStack.removeLastOrNull() })
    }

    navigation<StoryView> { key: StoryView ->
        val backStack = LocalNavBackStack.current
        StoryViewScreenHost(
            storyId = key.storyId,
            onBack = { backStack.removeLastOrNull() },
        )
    }

    navigation<Profile> { key: Profile ->
        val backStack = LocalNavBackStack.current
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

    navigation<Conversation> { key: Conversation ->
        val backStack = LocalNavBackStack.current
        ConversationScreenHost(
            otherUserId = key.userId,
            otherUserName = "User",
            onStartVoiceCall = { userId -> backStack.add(VoiceCall(userId)) },
            onStartVideoCall = { userId -> backStack.add(VideoCall(userId)) },
        )
    }

    navigation<VoiceCall> { key: VoiceCall ->
        val backStack = LocalNavBackStack.current
        VoiceCallScreenHost(
            otherUserId = key.userId,
            otherUserName = "User",
            onCallEnded = { backStack.removeLastOrNull() },
        )
    }

    navigation<VideoCall> { key: VideoCall ->
        val backStack = LocalNavBackStack.current
        VideoCallScreenHost(
            otherUserId = key.userId,
            otherUserName = "User",
            onCallEnded = { backStack.removeLastOrNull() },
        )
    }

    navigation<PostDetail> { key: PostDetail ->
        PostDetailScreen(
            postId = key.postId,
        )
    }

    navigation<Notifications> {
        NotificationsScreen()
    }

    navigation<Collections> {
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
