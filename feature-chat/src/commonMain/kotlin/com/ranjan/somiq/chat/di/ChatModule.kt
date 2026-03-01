package com.ranjan.somiq.chat.di

import com.ranjan.somiq.chat.data.repository.ChatRepositoryImpl
import com.ranjan.somiq.chat.domain.repository.ChatRepository
import com.ranjan.somiq.chat.domain.usecase.GetConversationsUseCase
import com.ranjan.somiq.chat.domain.usecase.GetMessagesUseCase
import com.ranjan.somiq.chat.domain.usecase.SendMessageUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val chatModule = module {
    factory<ChatRepository> {
        ChatRepositoryImpl(httpClient = get<HttpClient>())
    }
    factoryOf(::GetConversationsUseCase)
    factoryOf(::GetMessagesUseCase)
    factoryOf(::SendMessageUseCase)
}
