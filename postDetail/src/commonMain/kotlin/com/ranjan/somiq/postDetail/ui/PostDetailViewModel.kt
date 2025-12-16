package com.ranjan.somiq.postDetail.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.postDetail.domain.usecase.CreateCommentUseCase
import com.ranjan.somiq.postDetail.domain.usecase.GetCommentsUseCase
import com.ranjan.somiq.postDetail.domain.usecase.ToggleCommentLikeUseCase
import com.ranjan.somiq.postDetail.ui.PostDetailContract.Action
import com.ranjan.somiq.postDetail.ui.PostDetailContract.Effect
import com.ranjan.somiq.postDetail.ui.PostDetailContract.UiState
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postId: String,
    private val feedRepository: FeedRepository,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val toggleCommentLikeUseCase: ToggleCommentLikeUseCase
) : BaseViewModel<UiState, Action, Effect>(UiState()) {

    init {
        handleAction(Action.LoadPost)
        handleAction(Action.LoadComments)
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.LoadPost -> loadPost()
            is Action.LoadComments -> loadComments()
            is Action.UpdateCommentText -> {
                setState { copy(commentText = action.text) }
            }
            is Action.PostComment -> postComment()
            is Action.ToggleCommentLike -> toggleCommentLike(action.commentId)
            is Action.Refresh -> {
                loadPost()
                loadComments()
            }
        }
    }

    private fun loadPost() {
        setState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
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
    }

    private fun loadComments() {
        setState { copy(isLoadingComments = true) }
        viewModelScope.launch {
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
    }

    private fun postComment() {
        val commentText = state.value.commentText.trim()
        if (commentText.isEmpty()) return

        viewModelScope.launch {
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
    }

    private fun toggleCommentLike(commentId: String) {
        viewModelScope.launch {
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
}
