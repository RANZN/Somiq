package com.ranjan.somiq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.ranjan.somiq.navigation.AppNavGraph.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.polymorphic

private fun PolymorphicModuleBuilder<NavKey>.authNavKeys() {
    subclass(Splash::class, Splash.serializer())
    subclass(OnBoarding.Login::class, OnBoarding.Login.serializer())
    subclass(OnBoarding.Otp::class, OnBoarding.Otp.serializer())
    subclass(OnBoarding.CompleteProfile::class, OnBoarding.CompleteProfile.serializer())
}

private fun PolymorphicModuleBuilder<NavKey>.homeNavKeys() {
    subclass(HomeGraph::class, HomeGraph.serializer())
    subclass(Chat::class, Chat.serializer())
    subclass(Profile::class, Profile.serializer())
    subclass(PostDetail::class, PostDetail.serializer())
    subclass(Notifications::class, Notifications.serializer())
    subclass(CreatePostScreen::class, CreatePostScreen.serializer())
    subclass(CreateStoryScreen::class, CreateStoryScreen.serializer())
    subclass(StoryView::class, StoryView.serializer())
    subclass(Collections::class, Collections.serializer())
    subclass(Conversation::class, Conversation.serializer())
    subclass(VoiceCall::class, VoiceCall.serializer())
    subclass(VideoCall::class, VideoCall.serializer())
    subclass(Settings::class, Settings.serializer())
}

private val appNavBackStackConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            authNavKeys()
            homeNavKeys()
        }
    }
}

@Composable
fun rememberAppNavBackStack(startDestination: NavKey): NavBackStack<NavKey> =
    rememberNavBackStack(appNavBackStackConfig, startDestination)

fun NavBackStack<NavKey>.isHomeOnTop(): Boolean = lastOrNull() == HomeGraph
