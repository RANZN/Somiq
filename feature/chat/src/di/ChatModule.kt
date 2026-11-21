package com.ranjan.somiq.chat.di

import com.ranjan.somiq.chat.data.repository.ChatRepositoryImpl
import com.ranjan.somiq.chat.domain.repository.ChatRepository
import com.ranjan.somiq.chat.domain.usecase.GetConversationsUseCase
import com.ranjan.somiq.chat.domain.usecase.GetMessagesUseCase
import com.ranjan.somiq.chat.domain.usecase.SendMessageUseCase
import com.ranjan.somiq.chat.ui.chatlist.ChatListViewModel
import com.ranjan.somiq.chat.ui.conversation.ConversationViewModel
import com.ranjan.somiq.chat.ui.videocall.VideoCallViewModel
import com.ranjan.somiq.chat.ui.voicecall.VoiceCallViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    factory<ChatRepository> {
        ChatRepositoryImpl(httpClient = get<HttpClient>())
    }
    factoryOf(::GetConversationsUseCase)
    factoryOf(::GetMessagesUseCase)
    factoryOf(::SendMessageUseCase)

    viewModel { ChatListViewModel(getConversationsUseCase = get<GetConversationsUseCase>()) }
    viewModel { params ->
        ConversationViewModel(
            otherUserId = params.get(),
            otherUserName = params.get(),
            getMessagesUseCase = get(),
            sendMessageUseCase = get()
        )
    }
    viewModel { params ->
        VoiceCallViewModel(
            otherUserId = params.get(),
            otherUserName = params.get()
        )
    }
    viewModel { params ->
        VideoCallViewModel(
            otherUserId = params.get(),
            otherUserName = params.get()
        )
    }
}
