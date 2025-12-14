package com.ranjan.somiq.feed.di

import com.ranjan.somiq.feed.data.repository.FeedRepositoryImpl
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.domain.usecase.GetFeedUseCase
import com.ranjan.somiq.feed.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val feedModule = module {
    factory<FeedRepository> {
        FeedRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetFeedUseCase)
    factoryOf(::GetStoriesUseCase)
    factoryOf(::ToggleLikeUseCase)
    factoryOf(::ToggleBookmarkUseCase)
}
