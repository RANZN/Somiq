package com.ranjan.somiq.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ranjan.somiq.core.data.local.AuthStateManager
import com.ranjan.somiq.core.data.local.AuthStateManagerImpl
import com.ranjan.somiq.core.data.local.DeviceIdProvider
import com.ranjan.somiq.core.data.local.DeviceIdProviderImpl
import com.ranjan.somiq.core.data.local.TokenStorage
import com.ranjan.somiq.core.data.local.TokenStorageImpl
import com.ranjan.somiq.core.data.local.createTokenDataStore
import com.ranjan.somiq.core.data.network.TokenProvider
import com.ranjan.somiq.core.data.network.TokenProviderImpl
import com.ranjan.somiq.core.data.network.TokenRefresher
import com.ranjan.somiq.core.data.network.TokenRefresherImpl
import com.ranjan.somiq.core.data.network.provideAuthHttpClient
import com.ranjan.somiq.core.data.network.provideNonAuthHttpClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val NonAuthClient = named("nonAuthClient")

val networkModule = module {
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    single<HttpClient>(NonAuthClient) {
        provideNonAuthHttpClient()
    }

    single<DataStore<Preferences>> { createTokenDataStore() }
    singleOf(::TokenStorageImpl) bind TokenStorage::class
    single<DeviceIdProvider> { DeviceIdProviderImpl(get()) }

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

    single {
        AppImageLoaderFactory(
            httpClient = get()
        )
    }
}