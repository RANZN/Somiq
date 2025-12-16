package com.ranjan.somiq.splash

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect

object SplashContract {
    sealed interface Effect : BaseUiEffect {
        data class NavigateToHome(val isUpdateNeeded: Boolean) : Effect
        data class NavigateToLogin(val isUpdateNeeded: Boolean) : Effect
    }
}