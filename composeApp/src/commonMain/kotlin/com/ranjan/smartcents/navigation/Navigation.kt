package com.ranjan.smartcents.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ranjan.smartcents.home.HomeScreenHost
import com.ranjan.smartcents.login.LoginScreenHost
import com.ranjan.smartcents.navigation.Screen.Home
import com.ranjan.smartcents.quiz_intro.FinMasterQuiz
import com.ranjan.smartcents.quiz_intro.FounderQuiz
import com.ranjan.smartcents.quiz_intro.QuizInstruction
import com.ranjan.smartcents.quiz_intro.QuizzesHost
import com.ranjan.smartcents.presentation.quiz.QuizType
import com.ranjan.smartcents.signup.SignupScreenHost
import com.ranjan.smartcents.splash.SplashScreenHost
import com.ranjan.smartcents.quiz.QuizHostScreen
import com.ranjan.smartcents.quiz_result.QuizResultHost
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
