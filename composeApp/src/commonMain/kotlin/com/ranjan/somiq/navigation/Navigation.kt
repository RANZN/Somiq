package com.ranjan.somiq.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ranjan.somiq.home.HomeScreenHost
import com.ranjan.somiq.login.LoginScreenHost
import com.ranjan.somiq.navigation.Screen.Home
import com.ranjan.somiq.quiz_intro.FinMasterQuiz
import com.ranjan.somiq.quiz_intro.FounderQuiz
import com.ranjan.somiq.quiz_intro.QuizInstruction
import com.ranjan.somiq.quiz_intro.QuizzesHost
import com.ranjan.somiq.presentation.quiz.QuizType
import com.ranjan.somiq.signup.SignupScreenHost
import com.ranjan.somiq.splash.SplashScreenHost
import com.ranjan.somiq.quiz.QuizHostScreen
import com.ranjan.somiq.quiz_result.QuizResultHost
import kotlinx.serialization.json.Json

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val json = remember { Json { encodeDefaults = true } }
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

        navigation<Screen.HomeGraph>(startDestination = Home.Landing) {
            composable<Home.Landing> {
                HomeScreenHost(
                    navigateToMoneyInsights = { /* TODO */ },
                    navigateToMoneyQuotes = { /* TODO */ },
                    navigateToLearnFinance = { /* TODO */ },
                    navigateToQuizzes = { navController.navigateToQuizSelection() }
                )
            }
            composable<Home.MoneyInsights> {
                // TODO
            }
            composable<Home.MoneyQuotes> {
                // TODO
            }
            composable<Home.LearnFinance> {
                // TODO
            }
        }
        navigation<Home.QuizzesSelectionGraph>(startDestination = Screen.Quizzes.QuizzesSelection) {
            composable<Screen.Quizzes.QuizzesSelection> {
                QuizzesHost(
                    navigateToFinMasterQuiz = {
                        navController.navigateToFinMasterQuiz()
                    },
                    navigateToFounderQuiz = {
                        navController.navigateToFounderQuiz()
                    }
                )
            }

            composable<Screen.Quizzes.FinMasterQuiz> {
                FinMasterQuiz {
                    navController.navigateToQuizIntro(it)
                }
            }
            composable<Screen.Quizzes.FounderQuiz> {
                FounderQuiz {
                    navController.navigateToQuizIntro(QuizType.FOUNDER)
                }
            }
            composable<Screen.Quizzes.QuizInstruction> {
                val quizType = remember { it.toRoute<Screen.Quizzes.QuizInstruction>().quizType }
                QuizInstruction {
                    navController.navigateToQuizScreen(quizType)
                }
            }

            composable<Screen.Quizzes.QuizScreen> {
                val quizType = remember { it.toRoute<Screen.Quizzes.QuizScreen>().quizType }
                QuizHostScreen(
                    quizType = quizType,
                    navigateToQuizResult = {
                        val encodedAnswers = json.encodeToString(it)
                        navController.navigateToQuizResult(encodedAnswers)
                    }
                )
            }

            composable<Screen.Quizzes.QuizResult> {
                val encodedAnswers = remember { it.toRoute<Screen.Quizzes.QuizResult>().answerMap }
                val quizResult = json.decodeFromString<Map<Int, Boolean>>(encodedAnswers)
                QuizResultHost(quizResult)
            }
        }
    }
}
