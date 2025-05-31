package com.ranjan.smartcents.android.navigation


import androidx.navigation.NavHostController
import com.ranjan.smartcents.presentation.quiz.QuizType


fun NavHostController.navigateToHome() {
    navigate(Screen.HomeGraph) {
        popUpTo(Screen.OnBoardingGraph) {
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

fun NavHostController.navigateToQuizSelection() {
    navigate(Screen.Home.QuizzesSelectionGraph)
}

fun NavHostController.navigateToFinMasterQuiz() {
    navigate(Screen.Quizzes.FinMasterQuiz)
}

fun NavHostController.navigateToFounderQuiz() {
    navigate(Screen.Quizzes.FounderQuiz)
}

fun NavHostController.navigateToQuizIntro(quizType: QuizType) {
    navigate(Screen.Quizzes.QuizInstruction(quizType))
}

fun NavHostController.navigateToQuizScreen(quizType: QuizType) {
    navigate(Screen.Quizzes.QuizScreen(quizType))
}

fun NavHostController.navigateToQuizResult(answerMap: String) {
    navigate(Screen.Quizzes.QuizResult(answerMap)) {
        popUpTo(Screen.Quizzes.QuizzesSelection) {
            inclusive = false
        }
    }
}