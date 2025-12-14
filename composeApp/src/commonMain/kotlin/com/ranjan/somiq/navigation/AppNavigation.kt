package com.ranjan.somiq.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.ranjan.somiq.auth.ui.login.LoginScreenHost
import com.ranjan.somiq.auth.ui.signup.SignupScreenHost
import com.ranjan.somiq.core.presentation.navigation.Screen
import com.ranjan.somiq.core.presentation.navigation.navigateToHome
import com.ranjan.somiq.core.presentation.navigation.navigateToOnBoarding
import com.ranjan.somiq.core.presentation.navigation.navigateToSignUp
import com.ranjan.somiq.collections.CollectionsScreen
import com.ranjan.somiq.home.ui.HomeNavigationHost
import com.ranjan.somiq.notifications.NotificationsScreen
import com.ranjan.somiq.postDetail.ui.PostDetailScreen
import com.ranjan.somiq.splash.SplashScreenHost

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface {
        NavHost(
            navController = navController,
            modifier = modifier,
            startDestination = Screen.Splash
        ) {
            composable<Screen.Splash> {
                SplashScreenHost(
                    navigateToHome = { navController.navigateToHome() },
                    navigateToLogin = { navController.navigateToOnBoarding() }
                )
            }

            navigation<Screen.OnBoardingGraph>(startDestination = Screen.OnBoarding.Login) {
                composable<Screen.OnBoarding.Login> {
                    LoginScreenHost(
                        navigateToDashboard = { navController.navigateToHome() },
                        navigateToSignUp = { navController.navigateToSignUp() }
                    )
                }
                composable<Screen.OnBoarding.SignUp> {
                    SignupScreenHost(
                        navigateToHome = { navController.navigateToHome() }
                    )
                }
            }

            // Home graph with all home screens
            navigation<Screen.HomeGraph>(startDestination = Screen.Home.Feed) {
                composable<Screen.Home.Feed> {
                    HomeNavigationHost(
                        currentRoute = currentRoute,
                        onNavigate = { destination ->
                            navController.navigate(destination) {
                                popUpTo(Screen.Home.Feed) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToUser = { userId ->
                            navController.navigate(Screen.Home.Profile(userId))
                        },
                        onNavigateToPost = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToComments = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToStory = { storyId ->
                            // TODO: Navigate to story viewer when implemented
                        },
                        onNavigateToHashtag = { hashtag ->
                            // TODO: Navigate to hashtag page when implemented
                        },
                        onShowShareDialog = { postId ->
                            // TODO: Show share dialog
                        },
                        onShowMoreOptions = { postId ->
                            // TODO: Show more options dialog
                        },
                        onNavigateToEditProfile = { userId ->
                            // TODO: Navigate to edit profile screen when implemented
                        },
                        onNavigateToSettings = { userId ->
                            // TODO: Navigate to settings screen when implemented
                        },
                        onNavigateToFollowers = { userId ->
                            // TODO: Navigate to followers screen when implemented
                        },
                        onNavigateToFollowing = { userId ->
                            // TODO: Navigate to following screen when implemented
                        },
                        onNavigateToLogin = {
                            navController.navigateToOnBoarding()
                        }
                    )
                }
                composable<Screen.Home.Search> {
                    HomeNavigationHost(
                        currentRoute = currentRoute,
                        onNavigate = { destination ->
                            navController.navigate(destination) {
                                popUpTo(Screen.Home.Feed) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToUser = { userId ->
                            navController.navigate(Screen.Home.Profile(userId))
                        },
                        onNavigateToPost = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToComments = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToStory = { storyId ->
                            // TODO: Navigate to story viewer when implemented
                        },
                        onNavigateToHashtag = { hashtag ->
                            // TODO: Navigate to hashtag page when implemented
                        },
                        onShowShareDialog = { postId ->
                            // TODO: Show share dialog
                        },
                        onShowMoreOptions = { postId ->
                            // TODO: Show more options dialog
                        },
                        onNavigateToEditProfile = { userId ->
                            // TODO: Navigate to edit profile screen when implemented
                        },
                        onNavigateToSettings = { userId ->
                            // TODO: Navigate to settings screen when implemented
                        },
                        onNavigateToFollowers = { userId ->
                            // TODO: Navigate to followers screen when implemented
                        },
                        onNavigateToFollowing = { userId ->
                            // TODO: Navigate to following screen when implemented
                        },
                        onNavigateToLogin = {
                            navController.navigateToOnBoarding()
                        }
                    )
                }
                composable<Screen.Home.Reels> {
                    HomeNavigationHost(
                        currentRoute = currentRoute,
                        onNavigate = { destination ->
                            navController.navigate(destination) {
                                popUpTo(Screen.Home.Feed) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToUser = { userId ->
                            navController.navigate(Screen.Home.Profile(userId))
                        },
                        onNavigateToPost = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToComments = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToStory = { storyId ->
                            // TODO: Navigate to story viewer when implemented
                        },
                        onNavigateToHashtag = { hashtag ->
                            // TODO: Navigate to hashtag page when implemented
                        },
                        onShowShareDialog = { postId ->
                            // TODO: Show share dialog
                        },
                        onShowMoreOptions = { postId ->
                            // TODO: Show more options dialog
                        },
                        onNavigateToEditProfile = { userId ->
                            // TODO: Navigate to edit profile screen when implemented
                        },
                        onNavigateToSettings = { userId ->
                            // TODO: Navigate to settings screen when implemented
                        },
                        onNavigateToFollowers = { userId ->
                            // TODO: Navigate to followers screen when implemented
                        },
                        onNavigateToFollowing = { userId ->
                            // TODO: Navigate to following screen when implemented
                        },
                        onNavigateToLogin = {
                            navController.navigateToOnBoarding()
                        }
                    )
                }
                composable<Screen.Home.Profile> {
                    HomeNavigationHost(
                        currentRoute = currentRoute,
                        onNavigate = { destination ->
                            navController.navigate(destination) {
                                popUpTo(Screen.Home.Feed) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToUser = { userId ->
                            navController.navigate(Screen.Home.Profile(userId))
                        },
                        onNavigateToPost = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToComments = { postId ->
                            navController.navigate(Screen.PostDetail(postId))
                        },
                        onNavigateToStory = { storyId ->
                            // TODO: Navigate to story viewer when implemented
                        },
                        onNavigateToHashtag = { hashtag ->
                            // TODO: Navigate to hashtag page when implemented
                        },
                        onShowShareDialog = { postId ->
                            // TODO: Show share dialog
                        },
                        onShowMoreOptions = { postId ->
                            // TODO: Show more options dialog
                        },
                        onNavigateToEditProfile = { userId ->
                            // TODO: Navigate to edit profile screen when implemented
                        },
                        onNavigateToSettings = { userId ->
                            // TODO: Navigate to settings screen when implemented
                        },
                        onNavigateToFollowers = { userId ->
                            // TODO: Navigate to followers screen when implemented
                        },
                        onNavigateToFollowing = { userId ->
                            // TODO: Navigate to following screen when implemented
                        },
                        onNavigateToLogin = {
                            navController.navigateToOnBoarding()
                        }
                    )
                }
            }

            composable<Screen.PostDetail> { backStackEntry ->
                // Extract postId from route - type-safe navigation serializes it in the route
                val route = backStackEntry.destination.route ?: ""
                val postId = try {
                    // Try to extract from query parameter or path
                    val queryIndex = route.indexOf('?')
                    if (queryIndex >= 0) {
                        val query = route.substring(queryIndex + 1)
                        query.split("&").find { it.startsWith("postId=") }?.substringAfter("=")
                    } else {
                        // Try to extract from path if postId is in the path
                        val parts = route.split("/")
                        parts.lastOrNull()?.takeIf { it != "PostDetail" && it.isNotEmpty() }
                    } ?: ""
                } catch (e: Exception) {
                    ""
                }
                PostDetailScreen(
                    postId = postId,
                    viewModel = org.koin.compose.viewmodel.koinViewModel(
                        parameters = { org.koin.core.parameter.parametersOf(postId) }
                    )
                )
            }

            composable<Screen.Notifications> {
                NotificationsScreen()
            }

            composable<Screen.Collections> {
                CollectionsScreen()
            }
        }
    }

}
