package com.ranjan.somiq.core.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Navigation helper functions for Nav3.
 * These functions work with NavBackStack instead of NavHostController.
 */

/**
 * Navigate to HomeGraph, clearing the back stack first.
 */
fun <T : NavKey> NavBackStack<T>.navigateToHome() {
    clear()
    @Suppress("UNCHECKED_CAST")
    (this as NavBackStack<NavKey>).add(HomeGraph)
}

/**
 * Navigate to OnBoardingGraph, clearing the back stack first.
 */
fun <T : NavKey> NavBackStack<T>.navigateToOnBoarding() {
    clear()
    @Suppress("UNCHECKED_CAST")
    (this as NavBackStack<NavKey>).add(OnBoardingGraph)
}

/**
 * Navigate to SignUp screen within OnBoarding graph.
 */
fun <T : NavKey> NavBackStack<T>.navigateToSignUp() {
    @Suppress("UNCHECKED_CAST")
    (this as NavBackStack<NavKey>).add(OnBoarding.SignUp)
}