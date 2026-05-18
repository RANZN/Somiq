package com.ranjan.somiq.di

import com.ranjan.somiq.collections.data.CollectionRepositoryImpl
import com.ranjan.somiq.collections.domain.CollectionRepository
import com.ranjan.somiq.collections.domain.GetCollectionsUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val collectionsModule = module {
    factory<CollectionRepository> {
        CollectionRepositoryImpl(httpClient = get<HttpClient>())
    }
    factoryOf(::GetCollectionsUseCase)
}
