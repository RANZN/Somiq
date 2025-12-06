package com.ranjan.somiq.core.di

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

    // TokenProvider can be overridden in platform-specific modules (e.g., AndroidModules)
    single<TokenProvider> {
        TokenProviderImpl()
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