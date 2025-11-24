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
    sealed interface Home {

    }

}