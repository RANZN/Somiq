package com.ranjan.somiq.core.di

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

    single<HttpClient> {
        provideAuthHttpClient(
            tokenProvider = get(),
            tokenRefresher = get()
        )
    }
}