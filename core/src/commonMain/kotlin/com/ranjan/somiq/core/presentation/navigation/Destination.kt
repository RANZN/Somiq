package com.ranjan.somiq.core.presentation.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Splash : NavKey

@Serializable
object OnBoardingGraph : NavKey

@Serializable
sealed interface OnBoarding : NavKey {
    @Serializable
    data object Login : OnBoarding

    @Serializable
    data object SignUp : OnBoarding
}

@Serializable
data object HomeGraph : NavKey

@Stable
@Serializable
sealed class Home(val name: String) : NavKey {

    @Serializable
    data object ChatLists : Home("Chats")

    @Serializable
    data object Updates : Home("Updates")

    @Serializable
    data object Calls : Home("Calls")

    @Serializable
    data object UserProfile : Home("Profile")
}

@Serializable
data object Chat : NavKey

@Serializable
data class Profile(val userId: String) : NavKey

@Serializable
data class PostDetail(val postId: String) : NavKey

@Serializable
data object Notifications : NavKey

@Serializable
data object CreatePostScreen : NavKey

@Serializable
data object CreateStoryScreen : NavKey

@Serializable
data class StoryView(val storyId: String) : NavKey

@Serializable
data object Collections : NavKey

@Serializable
data class Conversation(val userId: String) : NavKey

@Serializable
data class VoiceCall(val userId: String) : NavKey

@Serializable
data class VideoCall(val userId: String) : NavKey
