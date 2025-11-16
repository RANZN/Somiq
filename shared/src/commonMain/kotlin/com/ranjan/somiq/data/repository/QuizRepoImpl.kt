package com.ranjan.somiq.data.repository

import com.ranjan.somiq.data.model.QuizQuestion
import com.ranjan.somiq.domain.repository.QuizRepo
import com.ranjan.somiq.presentation.quiz.QuizType
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