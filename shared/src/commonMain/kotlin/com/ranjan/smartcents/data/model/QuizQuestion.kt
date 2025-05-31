package com.ranjan.smartcents.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    @SerialName("question")
    val title: String,
    val options: List<String>,
    val correctOption: String,
)
