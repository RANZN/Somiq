package com.ranjan.somiq.postDetail.ui

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.postDetail.data.model.CommentResponse

object PostDetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val post: Post? = null,
        val comments: List<CommentResponse> = emptyList(),
        val isLoadingComments: Boolean = false,
        val error: String? = null,
        val commentText: String = ""
    ) : BaseUiState

    sealed class Action : BaseUiAction {
        data object LoadPost : Action()
        data object LoadComments : Action()
        data class UpdateCommentText(val text: String) : Action()
        data object PostComment : Action()
        data class ToggleCommentLike(val commentId: String) : Action()
        data object Refresh : Action()
    }

    sealed class Effect : BaseUiEffect {
        data class ShowError(val message: String) : Effect()
        data object CommentPosted : Effect()
    }
}
