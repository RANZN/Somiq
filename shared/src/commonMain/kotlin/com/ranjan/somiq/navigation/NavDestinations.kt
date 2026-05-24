package com.ranjan.somiq.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Splash : NavKey

@Serializable
sealed interface OnBoarding : NavKey {
    @Serializable
    data object Login : OnBoarding

    @Serializable
    data class Otp(val phone: String) : OnBoarding

    @Serializable
    data class CompleteProfile(val signupToken: String) : OnBoarding
}

@Serializable
data object HomeGraph : NavKey

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

@Serializable
data object Settings : NavKey
