package com.ranjan.smartcents.android.quiz_result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ranjan.smartcents.presentation.quiz.QuizType

@Composable
fun QuizResultHost(
    quizResult: Map<Int, Boolean>,
) {
    Column {
        LazyColumn {
            items(quizResult.keys.size) {
                val isCorrect = if (quizResult[it] == true) {
                    "Correct Answer"
                } else {
                    "Wrong Answer"
                }
                Text("Question ${it + 1}     $isCorrect")
            }
        }
    }
}