package com.ranjan.somiq.splash


sealed interface SplashEvents {
    data class NavigateToHome(val isUpdateNeeded: Boolean) : SplashEvents
    data class NavigateToLogin(val isUpdateNeeded: Boolean) : SplashEvents
}