package com.ranjan.somiq.app.search.di

import com.ranjan.somiq.app.search.data.repository.SearchRepositoryImpl
import com.ranjan.somiq.app.search.domain.repository.SearchRepository
import com.ranjan.somiq.app.search.domain.usecase.SearchUseCase
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
