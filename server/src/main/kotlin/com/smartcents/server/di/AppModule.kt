package com.smartcents.server.di

import com.smartcents.server.application.auth.AuthController
import com.smartcents.server.data.service.JwtTokenProvider
import com.smartcents.server.data.service.PasswordCipherImpl
import com.smartcents.server.data.repository.UserRepositoryImpl
import com.smartcents.server.domain.service.PasswordCipher
import com.smartcents.server.domain.service.TokenProvider
import com.smartcents.server.domain.repository.UserRepository
import com.smartcents.server.domain.usecase.LoginUserUseCase
import com.smartcents.server.domain.usecase.SignUpUserUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val appModule = module {
    singleOf(::AuthController)
    singleOf(::LoginUserUseCase)
    singleOf(::SignUpUserUseCase)
    singleOf<PasswordCipher>(::PasswordCipherImpl)
    singleOf<UserRepository>(::UserRepositoryImpl)

    single<TokenProvider> {
        val secret = "your-super-secret-for-jwt"
        val issuer = "your-issuer"
        val audience = "your-audience"
        JwtTokenProvider(secret, issuer, audience)
    }

}