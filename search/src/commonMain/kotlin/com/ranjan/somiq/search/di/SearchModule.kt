package com.ranjan.somiq.search.di

import com.ranjan.somiq.search.data.repository.SearchRepositoryImpl
import com.ranjan.somiq.search.domain.repository.SearchRepository
import com.ranjan.somiq.search.domain.usecase.SearchUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchModule = module {
    factory<SearchRepository> {
        SearchRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::SearchUseCase)
}
