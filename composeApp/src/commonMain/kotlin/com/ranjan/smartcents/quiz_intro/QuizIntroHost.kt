package com.ranjan.smartcents.android.quiz_intro

import androidx.compose.runtime.Composable

@Composable
fun QuizzesHost(
    navigateToFinMasterQuiz: () -> Unit,
    navigateToFounderQuiz: () -> Unit,
) {
    QuizzesScreen(
        navigateToFinMasterQuiz = navigateToFinMasterQuiz,
        navigateToFounderQuiz = navigateToFounderQuiz,
    )
}