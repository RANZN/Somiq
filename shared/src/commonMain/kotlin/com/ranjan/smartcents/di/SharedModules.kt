package com.ranjan.smartcents.di

import com.ranjan.smartcents.data.repository.AuthRepoImpl
import com.ranjan.smartcents.data.repository.QuizRepoImpl
import com.ranjan.smartcents.domain.repository.AuthRepo
import com.ranjan.smartcents.domain.repository.QuizRepo
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedModules = module {

    single(named("userDatabase")) {
        Firebase.firestore.collection("users")
    }

    factory<AuthRepo> {
        AuthRepoImpl(
            userDatabase = get(named("userDatabase"))
        )
    }

    single(named("questionDatabase")) {
        Firebase.firestore.collection("questions")
    }

    factory<QuizRepo> {
        QuizRepoImpl(
            questionDatabase = get(named("questionDatabase"))
        )
    }
}