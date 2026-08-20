package com.ranjan.somiq.app.postDetail.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.app.postDetail.domain.usecase.CreateCommentUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.GetCommentsUseCase
import com.ranjan.somiq.app.postDetail.domain.usecase.ToggleCommentLikeUseCase
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Effect
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.Intent
import com.ranjan.somiq.app.postDetail.ui.PostDetailContract.UiState
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.feed.domain.usecase.GetPostUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleBookmarkUseCase
import com.ranjan.somiq.feed.domain.usecase.ToggleLikeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postId: String,
    private val getPostUseCase: GetPostUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val toggleCommentLikeUseCase: ToggleCommentLikeUseCase
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.Initialize -> {
                    launch { loadPost() }
                    launch { loadComments() }
                }
                is Intent.UpdateCommentText -> {
                    _uiState.update {it.copy(commentText = intent.text) }
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
        _uiState.update {it.copy(isLoading = true, error = null) }
        getPostUseCase(postId).fold(
            onSuccess = { post ->
                _uiState.update {it.copy(post = post, isLoading = false) }
            },
            onFailure = { error ->
                val appError = error.toAppError(PostDetailContract.ScreenError.LoadPostFailed)
                _uiState.update {it.copy(isLoading = false, error = appError) }
                showSnackbar(appError)
            }
        )
    }
    private suspend fun toggleLike() {
        toggleLikeUseCase(postId).fold(
            onSuccess = { toggleResponse ->
                uiState.value.post?.let { post ->
                    _uiState.update {it.copy(
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
                uiState.value.post?.let { post ->
                    _uiState.update {it.copy(
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
        _uiState.update {it.copy(isLoadingComments = true) }
        getCommentsUseCase(postId = postId).fold(
            onSuccess = { comments ->
                _uiState.update {it.copy(comments = comments, isLoadingComments = false) }
            },
            onFailure = { error ->
                val appError = error.toAppError(PostDetailContract.ScreenError.LoadCommentsFailed)
                _uiState.update {it.copy(isLoadingComments = false, error = appError) }
                showSnackbar(appError)
            }
        )
    }
    private suspend fun postComment() {
        val commentText = uiState.value.commentText.trim()
        if (commentText.isEmpty()) return
        createCommentUseCase(postId = postId, content = commentText).fold(
            onSuccess = {
                _uiState.update {it.copy(commentText = "") }
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
