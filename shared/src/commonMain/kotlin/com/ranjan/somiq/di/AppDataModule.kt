package com.ranjan.somiq.di

import com.ranjan.somiq.collections.data.CollectionRepositoryImpl
import com.ranjan.somiq.collections.domain.CollectionRepository
import com.ranjan.somiq.collections.domain.GetCollectionsUseCase
import com.ranjan.somiq.splash.data.CheckForUpdateRepository
import com.ranjan.somiq.splash.data.CheckForUpdateRepositoryImpl
import com.ranjan.somiq.splash.data.CheckUpdateUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appDataModule = module {
    factoryOf(::CheckForUpdateRepositoryImpl) bind CheckForUpdateRepository::class
    factoryOf(::CheckUpdateUseCase)

    factory<CollectionRepository> {
        CollectionRepositoryImpl(httpClient = get<HttpClient>())
    }
    factoryOf(::GetCollectionsUseCase)
}
