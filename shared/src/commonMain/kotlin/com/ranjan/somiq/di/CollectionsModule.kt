package com.ranjan.somiq.di

import com.ranjan.somiq.collections.data.CollectionRepositoryImpl
import com.ranjan.somiq.collections.domain.CollectionRepository
import com.ranjan.somiq.collections.domain.GetCollectionsUseCase
import com.ranjan.somiq.collections.CollectionsViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val collectionsModule = module {
    factory<CollectionRepository> {
        CollectionRepositoryImpl(httpClient = get<HttpClient>())
    }
    factoryOf(::GetCollectionsUseCase)
    viewModelOf(::CollectionsViewModel)
}
