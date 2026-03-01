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
sealed interface Home : NavKey {
    @Serializable
    data object Feed : Home

    @Serializable
    data object Search : Home

    @Serializable
    data object Reels : Home

    @Serializable
    data class Profile(val userId: String? = null) : Home
}

@Serializable
data class PostDetail(val postId: String) : NavKey

@Serializable
data object Notifications : NavKey

@Serializable
data object CreatePostScreen : NavKey

@Serializable
data object ChatListScreen : NavKey

@Serializable
data object Collections : NavKey

@Serializable
data object Chat : NavKey

@Serializable
data class Conversation(val userId: String) : NavKey

@Serializable
data class VoiceCall(val userId: String) : NavKey

@Serializable
data class VideoCall(val userId: String) : NavKey
