package com.ranjan.smartcents.android.navigation

import com.ranjan.smartcents.presentation.quiz.QuizType
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    object Splash : Screen

    @Serializable
    object OnBoardingGraph : Screen

    @Serializable
    sealed interface OnBoarding : Screen {
        @Serializable
        data object Login : OnBoarding

        @Serializable
        data object SignUp : OnBoarding
    }

    @Serializable
    data object HomeGraph : Screen

    @Serializable
    sealed interface Home {

        @Serializable
        data object Landing : Home

        @Serializable
        data object MoneyInsights : Home

        @Serializable
        data object MoneyQuotes : Home

        @Serializable
        data object LearnFinance : Home

        @Serializable
        data object QuizzesSelectionGraph : Home
    }


    @Serializable
    sealed interface Quizzes : Screen {

        @Serializable
        data object QuizzesSelection : Quizzes

        @Serializable
        data object FinMasterQuiz : Quizzes

        @Serializable
        data object FounderQuiz : Quizzes

        @Serializable
        data class QuizInstruction(val quizType: QuizType) : Quizzes

        @Serializable
        data class QuizScreen(val quizType: QuizType) : Quizzes

        @Serializable
        data class QuizResult(val answerMap: String) : Quizzes
    }
}