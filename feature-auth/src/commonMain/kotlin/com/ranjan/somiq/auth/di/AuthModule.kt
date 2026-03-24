package com.ranjan.somiq.auth.di

import com.ranjan.somiq.auth.data.repository.AuthRepositoryImpl
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.auth.domain.usecase.CheckUserIdUseCase
import com.ranjan.somiq.auth.domain.usecase.CompleteSignupUseCase
import com.ranjan.somiq.auth.domain.usecase.LogoutUseCase
import com.ranjan.somiq.auth.domain.usecase.VerifyOtpUseCase
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import com.ranjan.somiq.core.di.NonAuthClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authModule = module {
    factory<AuthRepository> {
        AuthRepositoryImpl(
            nonAuthHttpClient = get(NonAuthClient),
            authHttpClient = get(),
            tokenProvider = get(),
            authStateManager = get()
        )
    }
    factoryOf(::UserLoginStatus)
    factoryOf(::VerifyOtpUseCase)
    factoryOf(::CompleteSignupUseCase)
    factoryOf(::CheckUserIdUseCase)
    factoryOf(::LogoutUseCase)
}
