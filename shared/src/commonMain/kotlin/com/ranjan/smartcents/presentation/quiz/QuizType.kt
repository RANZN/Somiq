package com.ranjan.smartcents.presentation.quiz

enum class QuizType {
    FIN_MASTER_JUNIOR,
    FIN_MASTER_SENIOR,
    FIN_MASTER_PRO,
    FOUNDER;

    val getName: String
        get() = when (this) {
            FIN_MASTER_JUNIOR -> "fin_junior"
            FIN_MASTER_SENIOR -> "fin_senior"
            FIN_MASTER_PRO -> "fin_pro"
            FOUNDER -> "founder"
        }
}