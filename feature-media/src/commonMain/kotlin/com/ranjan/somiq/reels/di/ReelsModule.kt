package com.ranjan.somiq.reels.di

import com.ranjan.somiq.reels.data.repository.ReelsRepositoryImpl
import com.ranjan.somiq.reels.domain.repository.ReelsRepository
import com.ranjan.somiq.reels.domain.usecase.GetReelsUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val reelsModule = module {
    factory<ReelsRepository> {
        ReelsRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetReelsUseCase)
}
