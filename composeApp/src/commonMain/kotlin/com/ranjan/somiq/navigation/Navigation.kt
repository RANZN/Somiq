package com.ranjan.somiq.core.presentation.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
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
import com.ranjan.somiq.home.ui.HomeScreenHost
import com.ranjan.somiq.home.ui.components.BottomNavigationBar
import com.ranjan.somiq.profile.ui.ProfileScreenHost
import com.ranjan.somiq.reels.ui.ReelsScreenHost
import com.ranjan.somiq.search.ui.SearchScreenHost
import com.ranjan.somiq.splash.SplashScreenHost

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Routes that should show bottom navigation bar
    val bottomBarRoutes = setOf(
        Screen.Home.Feed,
        Screen.Home.Search,
        Screen.Home.Reels,
        Screen.Home.Profile
    )

    // Check if current route should show bottom bar
    val showBottomBar = currentRoute?.let { route ->
        bottomBarRoutes.any { bottomRoute ->
            route.contains(bottomRoute::class.simpleName ?: "", ignoreCase = true)
        }
    } ?: false

    Scaffold(
        bottomBar = {

            if (showBottomBar) {
                BottomNavigationBar(
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
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    ) { paddingValues ->
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
                    HomeScreenHost(
                        onNavigateToUser = { userId ->
                            navController.navigate(Screen.Home.Profile)
                        },
                        onNavigateToPost = { postId ->
                            // TODO: Navigate to post detail screen when implemented
                        },
                        onNavigateToComments = { postId ->
                            // TODO: Navigate to comments screen when implemented
                        },
                        onNavigateToStory = { storyId ->
                            // TODO: Navigate to story viewer when implemented
                        },
                        onShowShareDialog = { postId ->
                            // TODO: Show share dialog
                        },
                        onShowMoreOptions = { postId ->
                            // TODO: Show more options dialog
                        }
                    )
                }
                composable<Screen.Home.Search> {
                    SearchScreenHost(
                        onNavigateToUser = { userId ->
                            navController.navigate(Screen.Home.Profile)
                        },
                        onNavigateToHashtag = { hashtag ->
                            // TODO: Navigate to hashtag page when implemented
                        },
                        onNavigateToPost = { postId ->
                            // TODO: Navigate to post detail screen when implemented
                        }
                    )
                }
                composable<Screen.Home.Reels> {
                    ReelsScreenHost(
                        onNavigateToReel = { reelId ->
                            // TODO: Navigate to reel detail screen when implemented
                        },
                        onNavigateToComments = { reelId ->
                            // TODO: Navigate to comments screen when implemented
                        },
                        onShowShareDialog = { reelId ->
                            // TODO: Show share dialog
                        }
                    )
                }
                composable<Screen.Home.Profile> {
                    ProfileScreenHost(
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
                        }
                    )
                }
            }
        }
    }
}
