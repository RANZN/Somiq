package com.ranjan.smartcents.data.repository

import com.ranjan.smartcents.data.model.QuizQuestion
import com.ranjan.smartcents.domain.repository.QuizRepo
import com.ranjan.smartcents.presentation.quiz.QuizType
import dev.gitlive.firebase.firestore.CollectionReference

class QuizRepoImpl(
    private val questionDatabase: CollectionReference
) : QuizRepo {

    override suspend fun getQuizQuestion(quizType: QuizType): List<QuizQuestion> {
        val questionList = questionDatabase.document(quizType.getName).get()
            .get<List<QuizQuestion>>("list")
        return questionList
    }
}