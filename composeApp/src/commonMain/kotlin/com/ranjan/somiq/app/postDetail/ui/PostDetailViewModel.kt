package com.ranjan.somiq.app.postDetail.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.app.postDetail.domain.usecase.CreateCommentUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.GetCommentsUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.ToggleCommentLikeUseCase
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Intent
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Effect
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.UiState
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postId: String,
    private val feedRepository: FeedRepository,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val toggleCommentLikeUseCase: ToggleCommentLikeUseCase
) : BaseViewModel<UiState, Intent, Effect>(UiState()) {

    init {
        handleIntent(Intent.LoadPost)
        handleIntent(Intent.LoadComments)
    }

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
            is Intent.LoadPost -> loadPost()
            is Intent.LoadComments -> loadComments()
            is Intent.UpdateCommentText -> {
                setState { copy(commentText = intent.text) }
            }
            is Intent.PostComment -> postComment()
            is Intent.ToggleCommentLike -> toggleCommentLike(intent.commentId)
            is Intent.Refresh -> {
                loadPost()
                loadComments()
            }
            }
        }
    }

    private suspend fun loadPost() {
        setState { copy(isLoading = true, error = null) }
        feedRepository.getPost(postId).fold(
            onSuccess = { post ->
                setState { copy(post = post, isLoading = false) }
            },
            onFailure = { error ->
                setState {
                    copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load post"
                    )
                }
            }
        )
    }

    private suspend fun loadComments() {
        setState { copy(isLoadingComments = true) }
        getCommentsUseCase(postId = postId).fold(
            onSuccess = { comments ->
                setState { copy(comments = comments, isLoadingComments = false) }
            },
            onFailure = { error ->
                setState {
                    copy(
                        isLoadingComments = false,
                        error = error.message ?: "Failed to load comments"
                    )
                }
            }
        )
    }

    private suspend fun postComment() {
        val commentText = state.value.commentText.trim()
        if (commentText.isEmpty()) return

        createCommentUseCase(postId = postId, content = commentText).fold(
            onSuccess = {
                setState { copy(commentText = "") }
                emitEffect(Effect.CommentPosted)
                loadComments()
            },
            onFailure = { error ->
                emitEffect(Effect.ShowError(
                    error.message ?: "Failed to post comment"
                ))
            }
        )
    }

    private suspend fun toggleCommentLike(commentId: String) {
        toggleCommentLikeUseCase(commentId).fold(
            onSuccess = {
                loadComments()
            },
            onFailure = { error ->
                emitEffect(Effect.ShowError(
                    error.message ?: "Failed to toggle like"
                ))
            }
        )
    }
}
