package com.ranjan.somiq.app.postDetail.ui

import com.ranjan.somiq.app.postDetail.data.model.CommentResponse
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_comments
import com.ranjan.somiq.core.resources.error_failed_to_load_post
import com.ranjan.somiq.core.resources.error_failed_to_post_comment
import com.ranjan.somiq.core.resources.error_failed_to_toggle_comment_like
import com.ranjan.somiq.feed.data.model.Post

object PostDetailContract {
    sealed class ScreenError : BaseScreenError {
        data object LoadPostFailed : ScreenError()
        data object LoadCommentsFailed : ScreenError()
        data object PostCommentFailed : ScreenError()
        data object ToggleCommentLikeFailed : ScreenError()

        override fun toUiText(): UiText? = when (this) {
            LoadPostFailed -> UiText.Resource(Res.string.error_failed_to_load_post)
            LoadCommentsFailed -> UiText.Resource(Res.string.error_failed_to_load_comments)
            PostCommentFailed -> UiText.Resource(Res.string.error_failed_to_post_comment)
            ToggleCommentLikeFailed -> UiText.Resource(Res.string.error_failed_to_toggle_comment_like)
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val post: Post? = null,
        val comments: List<CommentResponse> = emptyList(),
        val isLoadingComments: Boolean = false,
        val error: AppError? = null,
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
        data class ShowError(val message: AppError) : Effect()
        data object CommentPosted : Effect()
    }
}
