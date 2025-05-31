package com.ranjan.smartcents.domain.repository

import com.ranjan.smartcents.data.model.QuizQuestion
import com.ranjan.smartcents.presentation.quiz.QuizType

interface QuizRepo {

    suspend fun getQuizQuestion(quizType: QuizType): List<QuizQuestion>

}