package com.smartcents.server.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val id: Int,
    val title: String,
    val description: String,
    val questions: List<Question>,
    val difficulty: String,
    val category: String
)

@Serializable
data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)

@Serializable
data class QuizResult(
    val quizId: Int,
    val userId: String,
    val score: Int,
    val totalQuestions: Int,
    val completedAt: String
)