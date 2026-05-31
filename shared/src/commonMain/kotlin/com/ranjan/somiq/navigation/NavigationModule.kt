package com.ranjan.somiq.navigation

import com.ranjan.somiq.navigation.AppNavGraph.*
import org.koin.dsl.navigation3.navigation
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import com.ranjan.somiq.splash.SplashScreenHost

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {
    navigation<Splash> {
        val backStack = LocalNavBackStack.current
        SplashScreenHost(
            navigateToHome = {
                backStack.clear()
                backStack.add(HomeGraph)
            },
            navigateToLogin = {
                backStack.clear()
                backStack.add(OnBoarding.Login)
            },
        )
    }
}
