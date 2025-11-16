package com.ranjan.somiq.di

import com.ranjan.somiq.data.db.AppDatabase
import com.ranjan.somiq.data.db.dao.UserDao
import com.ranjan.somiq.data.repository.AppRepositoryImpl
import com.ranjan.somiq.data.repository.AuthRepositoryImpl
import com.ranjan.somiq.data.repository.QuizRepoImpl
import com.ranjan.somiq.domain.repository.AppRepository
import com.ranjan.somiq.domain.repository.AuthRepository
import com.ranjan.somiq.domain.repository.QuizRepo
import com.ranjan.somiq.domain.usecase.CheckUpdate
import com.ranjan.somiq.domain.usecase.UserLoginStatus
import com.ranjan.somiq.domain.usecase.LoginUseCase
import com.ranjan.somiq.domain.usecase.SignupUseCase
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