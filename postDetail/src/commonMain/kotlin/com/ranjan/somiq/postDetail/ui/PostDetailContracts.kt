package com.ranjan.somiq.postDetail.ui

import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.postDetail.data.model.CommentResponse

data class PostDetailUiState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val comments: List<CommentResponse> = emptyList(),
    val isLoadingComments: Boolean = false,
    val error: String? = null,
    val commentText: String = ""
)

sealed class PostDetailAction {
    data object LoadPost : PostDetailAction()
    data object LoadComments : PostDetailAction()
    data class UpdateCommentText(val text: String) : PostDetailAction()
    data object PostComment : PostDetailAction()
    data class ToggleCommentLike(val commentId: String) : PostDetailAction()
    data object Refresh : PostDetailAction()
}

sealed class PostDetailEvent {
    data class ShowError(val message: String) : PostDetailEvent()
    data object CommentPosted : PostDetailEvent()
}
