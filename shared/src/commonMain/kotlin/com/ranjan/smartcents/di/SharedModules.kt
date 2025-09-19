package com.ranjan.smartcents.di

import com.ranjan.smartcents.data.db.AppDatabase
import com.ranjan.smartcents.data.db.dao.UserDao
import com.ranjan.smartcents.data.repository.AppRepositoryImpl
import com.ranjan.smartcents.data.repository.AuthRepositoryImpl
import com.ranjan.smartcents.data.repository.QuizRepoImpl
import com.ranjan.smartcents.domain.repository.AppRepository
import com.ranjan.smartcents.domain.repository.AuthRepository
import com.ranjan.smartcents.domain.repository.QuizRepo
import com.ranjan.smartcents.domain.usecase.CheckUpdate
import com.ranjan.smartcents.domain.usecase.UserLoginStatus
import com.ranjan.smartcents.domain.usecase.LoginUseCase
import com.ranjan.smartcents.domain.usecase.SignupUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedModules = module {

    single(named("userDatabase")) {
        Firebase.firestore.collection("users")
    }

    single(named("questionDatabase")) {
        Firebase.firestore.collection("questions")
    }

    factory<QuizRepo> {
        QuizRepoImpl(
            questionDatabase = get(named("questionDatabase"))
        )
    }

    single<UserDao> { get<AppDatabase>().userDao() }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory<AppRepository> { AppRepositoryImpl(get()) }
    factoryOf(::LoginUseCase)
    factoryOf(::SignupUseCase)
    factoryOf(::UserLoginStatus)
    factoryOf(::CheckUpdate)
}