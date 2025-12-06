package com.ranjan.somiq.search.ui

import androidx.compose.runtime.Stable

@Stable
data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchActive: Boolean = false
) {
    val hasError: Boolean
        get() = error != null
    
    val hasQuery: Boolean
        get() = searchQuery.isNotBlank()
}

sealed interface SearchAction {
    data class OnQueryChange(val query: String) : SearchAction
    object PerformSearch : SearchAction
    object ClearSearch : SearchAction
    object ClearError : SearchAction
    object Retry : SearchAction
    data class OnUserClick(val userId: String) : SearchAction
    data class OnHashtagClick(val hashtag: String) : SearchAction
    data class OnPostClick(val postId: String) : SearchAction
}

sealed interface SearchEvent {
    data class NavigateToUser(val userId: String) : SearchEvent
    data class NavigateToHashtag(val hashtag: String) : SearchEvent
    data class NavigateToPost(val postId: String) : SearchEvent
}

