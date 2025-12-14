package com.ranjan.somiq.postDetail.di

import com.ranjan.somiq.postDetail.ui.PostDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val postDetailViewModelModule = module {
    viewModel { parameters ->
        PostDetailViewModel(
            postId = parameters.get(),
            feedRepository = get(),
            getCommentsUseCase = get(),
            createCommentUseCase = get(),
            toggleCommentLikeUseCase = get()
        )
    }
}
