package com.ranjan.somiq.search.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.search.data.model.SearchResult

object SearchContract {
    @Stable
    data class UiState(
        val searchQuery: String = "",
        val searchResults: SearchResult? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSearchActive: Boolean = false
    ) : BaseUiState {
        val hasError: Boolean
            get() = error != null

        val hasQuery: Boolean
            get() = searchQuery.isNotBlank()

        val hasResults: Boolean
            get() = searchResults != null && (
                    searchResults.users.isNotEmpty() ||
                            searchResults.posts.isNotEmpty() ||
                            searchResults.reels.isNotEmpty()
                    )
    }

    sealed interface Action : BaseUiAction {
        data class OnQueryChange(val query: String) : Action
        object PerformSearch : Action
        object ClearSearch : Action
        object ClearError : Action
        object Retry : Action
        data class OnUserClick(val userId: String) : Action
        data class OnHashtagClick(val hashtag: String) : Action
        data class OnPostClick(val postId: String) : Action
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToUser(val userId: String) : Effect
        data class NavigateToHashtag(val hashtag: String) : Effect
        data class NavigateToPost(val postId: String) : Effect
    }
}
