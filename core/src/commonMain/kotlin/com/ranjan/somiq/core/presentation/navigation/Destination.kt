package com.ranjan.somiq.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    object Splash : Screen

    @Serializable
    object OnBoardingGraph : Screen

    @Serializable
    sealed interface OnBoarding : Screen {
        @Serializable
        data object Login : OnBoarding

        @Serializable
        data object SignUp : OnBoarding
    }

    @Serializable
    data object HomeGraph : Screen

    @Serializable
    sealed interface Home : Screen {
        @Serializable
        data object Feed : Home
        
        @Serializable
        data object Search : Home
        
        @Serializable
        data object Reels : Home
        
        @Serializable
        data object Profile : Home
    }

    @Serializable
    data object PostDetail : Screen
}