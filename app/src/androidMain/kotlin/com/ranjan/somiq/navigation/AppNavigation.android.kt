package com.ranjan.somiq.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ranjan.somiq.app.home.ui.HomeNavigationHost
import com.ranjan.somiq.app.postDetail.ui.PostDetailScreen
import com.ranjan.somiq.auth.ui.login.LoginScreenHost
import com.ranjan.somiq.auth.ui.signup.SignupScreenHost
import com.ranjan.somiq.chat.ui.conversation.ConversationScreenHost
import com.ranjan.somiq.chat.ui.videocall.VideoCallScreenHost
import com.ranjan.somiq.chat.ui.voicecall.VoiceCallScreenHost
import com.ranjan.somiq.collections.CollectionsScreen
import com.ranjan.somiq.core.presentation.navigation.Collections
import com.ranjan.somiq.core.presentation.navigation.Conversation
import com.ranjan.somiq.core.presentation.navigation.Home
import com.ranjan.somiq.core.presentation.navigation.Notifications
import com.ranjan.somiq.core.presentation.navigation.OnBoarding
import com.ranjan.somiq.core.presentation.navigation.PostDetail
import com.ranjan.somiq.core.presentation.navigation.Splash
import com.ranjan.somiq.core.presentation.navigation.VideoCall
import com.ranjan.somiq.core.presentation.navigation.VoiceCall
import com.ranjan.somiq.notifications.NotificationsScreen
import com.ranjan.somiq.splash.SplashScreenHost
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
actual fun AppNavigation(modifier: Modifier) {
    // Use Android overload (reflection-based serialization); no polymorphic registration needed
    val backStack = rememberNavBackStack(Splash)

    val currentKey = backStack.lastOrNull()

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Splash> {
                SplashScreenHost(
                    navigateToHome = {
                        backStack.clear()
                        backStack.add(Home.Feed)
                    },
                    navigateToLogin = {
                        backStack.clear()
                        backStack.add(OnBoarding.Login)
                    }
                )
            }

            entry<OnBoarding.Login> {
                LoginScreenHost(
                    navigateToDashboard = {
                        backStack.clear()
                        backStack.add(Home.Feed)
                    },
                    navigateToSignUp = { backStack.add(OnBoarding.SignUp) }
                )
            }
            entry<OnBoarding.SignUp> {
                SignupScreenHost(
                    navigateToHome = {
                        backStack.clear()
                        backStack.add(Home.Feed)
                    }
                )
            }

            entry<Home.Feed> {
                HomeNavHost(backStack, currentKey)
            }
            entry<Home.Search> {
                HomeNavHost(backStack, currentKey)
            }
            entry<Home.CreatePost> {
                HomeNavHost(backStack, currentKey)
            }
            entry<Home.Reels> {
                HomeNavHost(backStack, currentKey)
            }
            entry<Home.Profile> {
                HomeNavHost(backStack, currentKey)
            }

            entry<Conversation> { key ->
                ConversationScreenHost(
                    otherUserId = key.userId,
                    otherUserName = "User",
                    onStartVoiceCall = { userId -> backStack.add(VoiceCall(userId)) },
                    onStartVideoCall = { userId -> backStack.add(VideoCall(userId)) }
                )
            }

            entry<VoiceCall> { key ->
                VoiceCallScreenHost(
                    otherUserId = key.userId,
                    otherUserName = "User",
                    onCallEnded = { backStack.removeLastOrNull() }
                )
            }

            entry<VideoCall> { key ->
                VideoCallScreenHost(
                    otherUserId = key.userId,
                    otherUserName = "User",
                    onCallEnded = { backStack.removeLastOrNull() }
                )
            }

            entry<PostDetail> { key ->
                PostDetailScreen(
                    postId = key.postId,
                    viewModel = koinViewModel(parameters = { parametersOf(key.postId) })
                )
            }

            entry<Notifications> {
                NotificationsScreen()
            }

            entry<Collections> {
                CollectionsScreen()
            }
        }
    )
}

@Composable
private fun HomeNavHost(
    backStack: NavBackStack<NavKey>,
    currentKey: NavKey?
) {
    HomeNavigationHost(
        currentKey = currentKey,
        onNavigate = { destination ->
            backStack.add(destination)
        },
        onNavigateToUser = { userId -> backStack.add(Home.Profile(userId)) },
        onNavigateToPost = { postId -> backStack.add(PostDetail(postId)) },
        onNavigateToComments = { postId -> backStack.add(PostDetail(postId)) },
        onNavigateToStory = { },
        onNavigateToHashtag = { },
        onShowShareDialog = { },
        onShowMoreOptions = { },
        onNavigateToEditProfile = { },
        onNavigateToSettings = { },
        onNavigateToFollowers = { },
        onNavigateToFollowing = { },
        onNavigateToConversation = { userId -> backStack.add(Conversation(userId)) },
        onNavigateToLogin = {
            backStack.clear()
            backStack.add(OnBoarding.Login)
        }
    )
}
