package com.ranjan.somiq.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.data.model.QuizQuestion
import com.ranjan.somiq.domain.repository.QuizRepo
import com.ranjan.somiq.util.toMinSecFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class QuizViewModel(
    private val repo: QuizRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun getQuizQuestion(quizType: QuizType) {
        viewModelScope.launch {
            val questionList = repo.getQuizQuestion(quizType)
            _uiState.update { it.copy(isLoading = false, questions = questionList) }
            startCountdown()
        }
    }

    private val timerDuration = 15.minutes.inWholeMilliseconds
    private var timerJob: Job? = null

    @OptIn(ExperimentalTime::class)
    private fun startCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            val startTime = Clock.System.now().toEpochMilliseconds()
            while (isActive) {
                val now = Clock.System.now().toEpochMilliseconds()
                val elapsed = now - startTime
                val remainingSeconds = (timerDuration - elapsed).coerceAtLeast(0) / 1000
                _uiState.update { it.copy(time = remainingSeconds.toMinSecFormat()) }
                delay(1000)
            }
        }
    }

    val selectedAnswers = mutableMapOf<Int, Boolean>()
    fun handleAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.SaveAnswer -> {
                    selectedAnswers[action.questionId] = action.answer == action.correctAnswer
                }

                else -> Unit
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }


    data class UiState(
        val isLoading: Boolean = true,
        val questions: List<QuizQuestion> = emptyList(),
        val time: String = ""
    )

    sealed interface Action {
        data object NavigateToQuizResult : Action
        data class SaveAnswer(
            val questionId: Int,
            val answer: String,
            val correctAnswer: String
        ) : Action
    }
}