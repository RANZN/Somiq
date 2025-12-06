package com.ranjan.somiq.home.di

import com.ranjan.somiq.home.data.repository.HomeRepositoryImpl
import com.ranjan.somiq.home.domain.repository.HomeRepository
import com.ranjan.somiq.home.domain.usecase.GetFeedUseCase
import com.ranjan.somiq.home.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.home.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.home.domain.usecase.ToggleBookmarkUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeModule = module {
    factory<HomeRepository> {
        HomeRepositoryImpl(
            httpClient = get<HttpClient>() // Gets the auth HttpClient from core
        )
    }
    factoryOf(::GetFeedUseCase)
    factoryOf(::GetStoriesUseCase)
    factoryOf(::ToggleLikeUseCase)
    factoryOf(::ToggleBookmarkUseCase)
}
