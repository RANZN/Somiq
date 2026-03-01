package com.ranjan.somiq.core.presentation.navigation

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

@Serializable
sealed interface Home : NavKey {
    @Serializable
    data object Feed : Home

    @Serializable
    data object Search : Home

    @Serializable
    data object Reels : Home

    @Serializable
    data object CreatePost : Home

    @Serializable
    data object Chat : Home

    @Serializable
    data class Profile(val userId: String? = null) : Home
}

@Serializable
data class PostDetail(val postId: String) : NavKey

@Serializable
data object Notifications : NavKey

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

// Companion object for type-safe navigation destinations
object Screen {
    val Splash = com.ranjan.somiq.core.presentation.navigation.Splash
    val OnBoardingGraph = com.ranjan.somiq.core.presentation.navigation.OnBoardingGraph
    val HomeGraph = com.ranjan.somiq.core.presentation.navigation.HomeGraph
    
    object OnBoarding {
        val Login = com.ranjan.somiq.core.presentation.navigation.OnBoarding.Login
        val SignUp = com.ranjan.somiq.core.presentation.navigation.OnBoarding.SignUp
    }
    
    object Home {
        val Feed = com.ranjan.somiq.core.presentation.navigation.Home.Feed
        val Search = com.ranjan.somiq.core.presentation.navigation.Home.Search
        val Reels = com.ranjan.somiq.core.presentation.navigation.Home.Reels
        val CreatePost = com.ranjan.somiq.core.presentation.navigation.Home.CreatePost
        val Chat = com.ranjan.somiq.core.presentation.navigation.Home.Chat
        fun Profile(userId: String? = null) = com.ranjan.somiq.core.presentation.navigation.Home.Profile(userId)
    }
    
    fun PostDetail(postId: String) = com.ranjan.somiq.core.presentation.navigation.PostDetail(postId)
    val Notifications = com.ranjan.somiq.core.presentation.navigation.Notifications
    val Collections = com.ranjan.somiq.core.presentation.navigation.Collections
    val Chat = com.ranjan.somiq.core.presentation.navigation.Chat
    fun Conversation(userId: String) = com.ranjan.somiq.core.presentation.navigation.Conversation(userId)
    fun VoiceCall(userId: String) = com.ranjan.somiq.core.presentation.navigation.VoiceCall(userId)
    fun VideoCall(userId: String) = com.ranjan.somiq.core.presentation.navigation.VideoCall(userId)
}
