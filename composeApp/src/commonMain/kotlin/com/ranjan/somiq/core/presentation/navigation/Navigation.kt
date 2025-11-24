package com.ranjan.somiq.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.ranjan.somiq.auth.ui.login.LoginScreenHost
import com.ranjan.somiq.auth.ui.signup.SignupScreenHost
import com.ranjan.somiq.splash.SplashScreenHost

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, modifier = modifier, startDestination = Screen.Splash) {
        composable<Screen.Splash> {
            SplashScreenHost(
                navigateToHome = { navController.navigateToHome() },
                navigateToLogin = { navController.navigateToOnBoarding() }
            )
        }

        navigation<Screen.OnBoardingGraph>(startDestination = Screen.OnBoarding.Login) {
            composable<Screen.OnBoarding.Login> {
                LoginScreenHost(
                    onLoginSuccess = { navController.navigateToHome() },
                    onSignUpClick = { navController.navigateToSignUp() }
                )
            }
            composable<Screen.OnBoarding.SignUp> {
                SignupScreenHost(
                    navigateToHome = { navController.navigateToHome() }
                )
            }
        }
    }
}
