package com.ranjan.smartcents.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ranjan.smartcents.presentation.quiz.QuizType
import com.ranjan.smartcents.presentation.quiz.QuizViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun QuizHostScreen(
    quizType: QuizType,
    navigateToQuizResult: (MutableMap<Int, Boolean>) -> Unit
) {
    val viewmodel = remember { getKoin().get<QuizViewModel>() }
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewmodel.getQuizQuestion(quizType)
    }
    QuizScreen(uiState) {
        when (it) {
            QuizViewModel.Action.NavigateToQuizResult -> navigateToQuizResult(viewmodel.selectedAnswers)
            else -> viewmodel.handleAction(it)
        }
    }
}