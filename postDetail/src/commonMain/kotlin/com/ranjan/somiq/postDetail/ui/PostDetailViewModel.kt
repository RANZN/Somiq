package com.ranjan.somiq.postDetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.postDetail.domain.usecase.CreateCommentUseCase
import com.ranjan.somiq.postDetail.domain.usecase.GetCommentsUseCase
import com.ranjan.somiq.postDetail.domain.usecase.ToggleCommentLikeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postId: String,
    private val feedRepository: FeedRepository,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val toggleCommentLikeUseCase: ToggleCommentLikeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<PostDetailEvent?>(null)
    val events: StateFlow<PostDetailEvent?> = _events.asStateFlow()

    init {
        handleAction(PostDetailAction.LoadPost)
        handleAction(PostDetailAction.LoadComments)
    }

    fun handleAction(action: PostDetailAction) {
        when (action) {
            is PostDetailAction.LoadPost -> loadPost()
            is PostDetailAction.LoadComments -> loadComments()
            is PostDetailAction.UpdateCommentText -> {
                _uiState.update { it.copy(commentText = action.text) }
            }
            is PostDetailAction.PostComment -> postComment()
            is PostDetailAction.ToggleCommentLike -> toggleCommentLike(action.commentId)
            is PostDetailAction.Refresh -> {
                loadPost()
                loadComments()
            }
        }
    }

    private fun loadPost() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            feedRepository.getPost(postId).fold(
                onSuccess = { post ->
                    _uiState.update { it.copy(post = post, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load post"
                        )
                    }
                }
            )
        }
    }

    private fun loadComments() {
        _uiState.update { it.copy(isLoadingComments = true) }
        viewModelScope.launch {
            getCommentsUseCase(postId = postId).fold(
                onSuccess = { comments ->
                    _uiState.update { it.copy(comments = comments, isLoadingComments = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingComments = false,
                            error = error.message ?: "Failed to load comments"
                        )
                    }
                }
            )
        }
    }

    private fun postComment() {
        val commentText = _uiState.value.commentText.trim()
        if (commentText.isEmpty()) return

        viewModelScope.launch {
            createCommentUseCase(postId = postId, content = commentText).fold(
                onSuccess = {
                    _uiState.update { it.copy(commentText = "") }
                    _events.value = PostDetailEvent.CommentPosted
                    loadComments()
                },
                onFailure = { error ->
                    _events.value = PostDetailEvent.ShowError(
                        error.message ?: "Failed to post comment"
                    )
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
                    _events.value = PostDetailEvent.ShowError(
                        error.message ?: "Failed to toggle like"
                    )
                }
            )
        }
    }

    fun clearEvent() {
        _events.value = null
    }
}
