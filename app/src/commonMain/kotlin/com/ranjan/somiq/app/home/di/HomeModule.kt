package com.ranjan.somiq.app.home.di

import com.ranjan.somiq.app.home.data.repository.NotificationRepositoryImpl
import com.ranjan.somiq.app.home.domain.repository.NotificationRepository
import com.ranjan.somiq.app.home.domain.usecase.GetNotificationsUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeModule = module {
    factory<NotificationRepository> {
        NotificationRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetNotificationsUseCase)
}
