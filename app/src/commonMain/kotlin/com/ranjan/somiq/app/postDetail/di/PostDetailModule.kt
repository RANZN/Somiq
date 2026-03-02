package com.ranjan.somiq.app.postDetail.di

import com.ranjan.somiq.app.postDetail.data.repository.CommentRepositoryImpl
import com.ranjan.somiq.app.postDetail.domain.repository.CommentRepository
import com.ranjan.somiq.app.postDetail.domain.usecase.CreateCommentUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.GetCommentsUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.ToggleCommentLikeUseCase
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val postDetailModule = module {
    factory<CommentRepository> {
        CommentRepositoryImpl(
            httpClient = get<HttpClient>()
        )
    }
    factoryOf(::GetCommentsUseCase)
    factoryOf(::CreateCommentUseCase)
    factoryOf(::ToggleCommentLikeUseCase)
}
