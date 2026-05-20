package com.ranjan.somiq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.PolymorphicModuleBuilder

private fun PolymorphicModuleBuilder<NavKey>.authNavKeys() {
    subclass(OnBoarding.Login::class, OnBoarding.Login.serializer())
    subclass(OnBoarding.Otp::class, OnBoarding.Otp.serializer())
    subclass(OnBoarding.CompleteProfile::class, OnBoarding.CompleteProfile.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.homeNavKeys() {
    subclass(Home.Updates::class, Home.Updates.serializer())
    subclass(Home.UserProfile::class, Home.UserProfile.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.chatNavKeys() {
    subclass(Chat::class, Chat.serializer())
    subclass(Conversation::class, Conversation.serializer())
    subclass(VoiceCall::class, VoiceCall.serializer())
    subclass(VideoCall::class, VideoCall.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.rootAndOtherNavKeys() {
    subclass(Splash::class, Splash.serializer())
    subclass(HomeGraph::class, HomeGraph.serializer())
    subclass(PostDetail::class, PostDetail.serializer())
    subclass(Notifications::class, Notifications.serializer())
    subclass(CreatePostScreen::class, CreatePostScreen.serializer())
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
fun rememberAppNavBackStack(startDestination: NavKey = Splash): NavBackStack<NavKey> =
    rememberNavBackStack(navBackStackConfig, startDestination)
