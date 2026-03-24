package com.ranjan.somiq.core.di

import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.local.AuthStateManagerImpl
import com.ranjan.somiq.core.data.local.createTokenStorage
import com.ranjan.somiq.core.data.local.TokenStorage
import com.ranjan.somiq.core.data.network.TokenProvider
import com.ranjan.somiq.core.data.network.TokenProviderImpl
import com.ranjan.somiq.core.data.network.TokenRefresher
import com.ranjan.somiq.core.data.network.TokenRefresherImpl
import com.ranjan.somiq.core.data.network.provideAuthHttpClient
import com.ranjan.somiq.core.data.network.provideNonAuthHttpClient
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val NonAuthClient = named("nonAuthClient")

val networkModule = module {

    single<HttpClient>(NonAuthClient) {
        provideNonAuthHttpClient()
    }

    single<TokenStorage> {
        createTokenStorage()
    }

    singleOf(::TokenProviderImpl) bind TokenProvider::class
    singleOf(::AuthStateManagerImpl) bind AuthStateManager::class

    single<TokenRefresher> {
        TokenRefresherImpl(
            tokenProvider = get(),
            authStateManager = get(),
            nonAuthHttpClient = get(NonAuthClient)
        )
    }

    single<HttpClient> {
        provideAuthHttpClient(
            tokenProvider = get(),
            tokenRefresher = get()
        )
    }
}