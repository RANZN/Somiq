package com.ranjan.somiq.core.presentation.navigation


import androidx.navigation.NavHostController


fun NavHostController.navigateToHome() {
    navigate(Screen.HomeGraph) {
        popUpTo(Screen.Splash) {
            inclusive = true
        }
    }
}

fun NavHostController.navigateToOnBoarding() {
    navigate(Screen.OnBoardingGraph) {
        popUpTo(Screen.Splash) {
            inclusive = true
        }
    }
}

fun NavHostController.navigateToSignUp() {
    navigate(Screen.OnBoarding.SignUp) {
    }
}