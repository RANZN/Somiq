package com.ranjan.somiq.feed.di

import com.ranjan.somiq.feed.data.repository.FeedRepositoryImpl
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.domain.usecase.CreatePostUseCase
import com.ranjan.somiq.feed.domain.usecase.GetFeedPageUseCase
import com.ranjan.somiq.feed.domain.usecase.GetStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.feed.domain.usecase.GetPostsByUserUseCase
import com.ranjan.somiq.feed.domain.usecase.GetMyStoriesUseCase
import com.ranjan.somiq.feed.domain.usecase.GetBookmarkedPostsUseCase
import com.ranjan.somiq.feed.domain.usecase.GetPostUseCase
import com.ranjan.somiq.feed.ui.FeedViewModel
import com.ranjan.somiq.feed.ui.storyview.StoryViewViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val feedModule = module {
    factory<FeedRepository> {
        FeedRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetFeedPageUseCase)
    factoryOf(::GetStoriesUseCase)
    factoryOf(::ToggleLikeUseCase)
    factoryOf(::ToggleBookmarkUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::GetPostsByUserUseCase)
    factoryOf(::GetMyStoriesUseCase)
    factoryOf(::GetBookmarkedPostsUseCase)
    factoryOf(::GetPostUseCase)

    viewModelOf(::FeedViewModel)
    viewModelOf(::StoryViewViewModel)
}
