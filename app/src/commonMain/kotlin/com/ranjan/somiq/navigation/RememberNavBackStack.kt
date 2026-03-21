package com.ranjan.somiq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.ranjan.somiq.core.presentation.navigation.Chat
import com.ranjan.somiq.core.presentation.navigation.ChatListScreen
import com.ranjan.somiq.core.presentation.navigation.Collections
import com.ranjan.somiq.core.presentation.navigation.Conversation
import com.ranjan.somiq.core.presentation.navigation.CreatePostScreen
import com.ranjan.somiq.core.presentation.navigation.Home
import com.ranjan.somiq.core.presentation.navigation.HomeGraph
import com.ranjan.somiq.core.presentation.navigation.Notifications
import com.ranjan.somiq.core.presentation.navigation.OnBoarding
import com.ranjan.somiq.core.presentation.navigation.OnBoardingGraph
import com.ranjan.somiq.core.presentation.navigation.PostDetail
import com.ranjan.somiq.core.presentation.navigation.Splash
import com.ranjan.somiq.core.presentation.navigation.VideoCall
import com.ranjan.somiq.core.presentation.navigation.VoiceCall
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.PolymorphicModuleBuilder

private fun PolymorphicModuleBuilder<NavKey>.authNavKeys() {
    subclass(OnBoarding.Login::class, OnBoarding.Login.serializer())
    subclass(OnBoarding.SignUp::class, OnBoarding.SignUp.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.homeNavKeys() {
    subclass(Home.Feed::class, Home.Feed.serializer())
    subclass(Home.Search::class, Home.Search.serializer())
    subclass(Home.Reels::class, Home.Reels.serializer())
    subclass(Home.Profile::class, Home.Profile.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.chatNavKeys() {
    subclass(Chat::class, Chat.serializer())
    subclass(Conversation::class, Conversation.serializer())
    subclass(VoiceCall::class, VoiceCall.serializer())
    subclass(VideoCall::class, VideoCall.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.rootAndOtherNavKeys() {
    subclass(Splash::class, Splash.serializer())
    subclass(OnBoardingGraph::class, OnBoardingGraph.serializer())
    subclass(HomeGraph::class, HomeGraph.serializer())
    subclass(PostDetail::class, PostDetail.serializer())
    subclass(Notifications::class, Notifications.serializer())
    subclass(CreatePostScreen::class, CreatePostScreen.serializer())
    subclass(ChatListScreen::class, ChatListScreen.serializer())
    subclass(Collections::class, Collections.serializer())
}

/**
 * Single implementation for all platforms (Android, JVM, iOS).
 * Uses the two-arg rememberNavBackStack(config, initialKey) so the same API works
 * everywhere: Android doesn't have reflection for NavKey on non-Android targets,
 * so polymorphic serialization via SavedStateConfiguration is required for KMP.
 */
private val navBackStackConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            rootAndOtherNavKeys()
            authNavKeys()
            homeNavKeys()
            chatNavKeys()
        }
    }
}

@Composable
fun rememberAppNavBackStack(): NavBackStack<NavKey> =
    rememberNavBackStack(navBackStackConfig, Splash)
