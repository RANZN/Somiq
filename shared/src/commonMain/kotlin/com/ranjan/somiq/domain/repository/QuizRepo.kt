package com.ranjan.somiq.domain.repository

import com.ranjan.somiq.data.model.QuizQuestion
import com.ranjan.somiq.presentation.quiz.QuizType

interface QuizRepo {

    suspend fun getQuizQuestion(quizType: QuizType): List<QuizQuestion>

}