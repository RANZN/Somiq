package com.ranjan.somiq.auth.di

import com.ranjan.somiq.auth.data.repositories.AuthRepositoryImpl
import com.ranjan.somiq.auth.data.repository.ProfileRepositoryImpl
import com.ranjan.somiq.auth.domain.repository.AuthRepository
import com.ranjan.somiq.auth.domain.repository.ProfileRepository
import com.ranjan.somiq.auth.domain.usecase.GetProfileUseCase
import com.ranjan.somiq.auth.domain.usecase.LoginUseCase
import com.ranjan.somiq.auth.domain.usecase.SignupUseCase
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import com.ranjan.somiq.core.di.NonAuthClient
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
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
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    factoryOf(::UserLoginStatus)
    factoryOf(::SignupUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::GetProfileUseCase)
}
