package com.ranjan.somiq.app.postDetail.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.usecase.GetPostUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.CreateCommentUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.GetCommentsUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.ToggleCommentLikeUseCase
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Intent
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Effect
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.UiState
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postId: String,
    private val getPostUseCase: GetPostUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val toggleCommentLikeUseCase: ToggleCommentLikeUseCase
) : BaseViewModel<UiState, Intent, Effect>(UiState()) {

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.Initialize -> {
                    launch { loadPost() }
                    launch { loadComments() }
                }
                is Intent.UpdateCommentText -> {
                    setState { copy(commentText = intent.text) }
                }

                is Intent.PostComment -> postComment()
                is Intent.ToggleCommentLike -> toggleCommentLike(intent.commentId)
                is Intent.ToggleLike -> toggleLike()
                is Intent.ToggleBookmark -> toggleBookmark()
                is Intent.Refresh -> {
                    launch { loadPost() }
                    launch { loadComments() }
                }
            }
        }
    }

    private suspend fun loadPost() {
        setState { copy(isLoading = true, error = null) }
        getPostUseCase(postId).fold(
            onSuccess = { post ->
                setState { copy(post = post, isLoading = false) }
            },
            onFailure = { error ->
                val appError = error.toAppError(PostDetailContract.ScreenError.LoadPostFailed)
                setState { copy(isLoading = false, error = appError) }
                showSnackbar(appError)
            }
        )
    }

    private suspend fun toggleLike() {
        toggleLikeUseCase(postId).fold(
            onSuccess = { toggleResponse ->
                state.value.post?.let { post ->
                    setState {
                        copy(
                            post = post.copy(
                                isLiked = toggleResponse.isLiked,
                                likesCount = toggleResponse.likesCount
                            )
                        )
                    }
                }
            },
            onFailure = { error ->
                showSnackbar(error.toAppError(PostDetailContract.ScreenError.LoadPostFailed))
            }
        )
    }

    private suspend fun toggleBookmark() {
        toggleBookmarkUseCase(postId).fold(
            onSuccess = { toggleResponse ->
                state.value.post?.let { post ->
                    setState {
                        copy(
                            post = post.copy(
                                isBookmarked = toggleResponse.isBookmarked,
                                bookmarksCount = toggleResponse.bookmarksCount
                            )
                        )
                    }
                }
            },
            onFailure = { error ->
                showSnackbar(error.toAppError(PostDetailContract.ScreenError.LoadPostFailed))
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
                val appError = error.toAppError(PostDetailContract.ScreenError.LoadCommentsFailed)
                setState { copy(isLoadingComments = false, error = appError) }
                showSnackbar(appError)
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
                showSnackbar(
                    error.toAppError(PostDetailContract.ScreenError.PostCommentFailed),
                )
            }
        )
    }

    private suspend fun toggleCommentLike(commentId: String) {
        toggleCommentLikeUseCase(commentId).fold(
            onSuccess = {
                loadComments()
            },
            onFailure = { error ->
                showSnackbar(
                    error.toAppError(PostDetailContract.ScreenError.ToggleCommentLikeFailed),
                )
            }
        )
    }
}
