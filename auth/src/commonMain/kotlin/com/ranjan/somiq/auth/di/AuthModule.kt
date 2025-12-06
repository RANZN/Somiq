package com.ranjan.somiq.auth.di

import com.ranjan.somiq.auth.data.repositories.AuthRepositoryImpl
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.auth.domain.usecase.LoginUseCase
import com.ranjan.somiq.auth.domain.usecase.SignupUseCase
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    factoryOf(::AuthRepositoryImpl) bind AuthRepository::class
    factoryOf(::UserLoginStatus)
    factoryOf(::SignupUseCase)
    factoryOf(::LoginUseCase)
}
