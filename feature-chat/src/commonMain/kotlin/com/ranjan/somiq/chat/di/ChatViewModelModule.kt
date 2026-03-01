package com.ranjan.somiq.chat.di

import com.ranjan.somiq.chat.domain.usecase.GetConversationsUseCase
import com.ranjan.somiq.chat.domain.usecase.GetMessagesUseCase
import com.ranjan.somiq.chat.domain.usecase.SendMessageUseCase
import com.ranjan.somiq.chat.ui.chatlist.ChatListViewModel
import com.ranjan.somiq.chat.ui.conversation.ConversationViewModel
import com.ranjan.somiq.chat.ui.voicecall.VoiceCallViewModel
import com.ranjan.somiq.chat.ui.videocall.VideoCallViewModel
import org.koin.dsl.module

val chatViewModelModule = module {
    factory { ChatListViewModel(getConversationsUseCase = get<GetConversationsUseCase>()) }
    factory { params ->
        ConversationViewModel(
            otherUserId = params.get(),
            otherUserName = params.get(),
            getMessagesUseCase = get(),
            sendMessageUseCase = get()
        )
    }
    factory { params ->
        VoiceCallViewModel(
            otherUserId = params.get(),
            otherUserName = params.get()
        )
    }
    factory { params ->
        VideoCallViewModel(
            otherUserId = params.get(),
            otherUserName = params.get()
        )
    }
}
