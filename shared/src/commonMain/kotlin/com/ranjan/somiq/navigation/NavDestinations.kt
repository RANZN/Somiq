package com.ranjan.somiq.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppNavGraph : NavKey {
    @Serializable
    data object Splash : AppNavGraph

    @Serializable
    sealed interface OnBoarding : AppNavGraph {
        @Serializable
        data object Login : OnBoarding

        @Serializable
        data class Otp(val phone: String) : OnBoarding

        @Serializable
        data class CompleteProfile(val signupToken: String) : OnBoarding
    }

    @Serializable
    data object HomeGraph : AppNavGraph

    @Serializable
    data object Chat : AppNavGraph

    @Serializable
    data class Profile(val userId: String) : AppNavGraph

    @Serializable
    data class PostDetail(val postId: String) : AppNavGraph

    @Serializable
    data object Notifications : AppNavGraph

    @Serializable
    data object CreatePostScreen : AppNavGraph

    @Serializable
    data object CreateStoryScreen : AppNavGraph

    @Serializable
    data class StoryView(val storyId: String) : AppNavGraph

    @Serializable
    data object Collections : AppNavGraph

    @Serializable
    data class Conversation(val userId: String) : AppNavGraph

    @Serializable
    data class VoiceCall(val userId: String) : AppNavGraph

    @Serializable
    data class VideoCall(val userId: String) : AppNavGraph

    @Serializable
    data object Settings : AppNavGraph
}
