package com.ranjan.somiq.core.di

import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.local.AuthStateManagerImpl
import com.ranjan.somiq.core.data.network.TokenProvider
import com.ranjan.somiq.core.data.network.TokenProviderImpl
import com.ranjan.somiq.core.data.network.TokenRefresher
import com.ranjan.somiq.core.data.network.TokenRefresherImpl
import com.ranjan.somiq.core.data.network.provideAuthHttpClient
import com.ranjan.somiq.core.data.network.provideNonAuthHttpClient
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val NonAuthClient = named("nonAuthClient")

val networkModule = module {

    single<HttpClient>(NonAuthClient) {
        provideNonAuthHttpClient()
    }

    single<TokenProvider> {
        TokenProviderImpl()
    }

    single<AuthStateManager> {
        AuthStateManagerImpl()
    }

    single<TokenRefresher> {
        TokenRefresherImpl(
            tokenProvider = get(),
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