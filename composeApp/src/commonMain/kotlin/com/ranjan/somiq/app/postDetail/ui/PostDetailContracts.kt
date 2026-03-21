package com.ranjan.somiq.app.postDetail.ui

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.app.postDetail.data.model.CommentResponse

object PostDetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val post: Post? = null,
        val comments: List<CommentResponse> = emptyList(),
        val isLoadingComments: Boolean = false,
        val error: String? = null,
        val commentText: String = ""
    ) : BaseUiState

    sealed class Intent : BaseUiIntent {
        data object LoadPost : Intent()
        data object LoadComments : Intent()
        data class UpdateCommentText(val text: String) : Intent()
        data object PostComment : Intent()
        data class ToggleCommentLike(val commentId: String) : Intent()
        data object Refresh : Intent()
    }

    sealed class Effect : BaseUiEffect {
        data class ShowError(val message: String) : Effect()
        data object CommentPosted : Effect()
    }
}
