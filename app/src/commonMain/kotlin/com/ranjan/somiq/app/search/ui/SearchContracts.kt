package com.ranjan.somiq.app.search.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.app.search.data.model.SearchResult

object SearchContract {
    @Stable
    data class UiState(
        val searchQuery: String = "",
        val searchResults: SearchResult? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSearchActive: Boolean = false,
        val showSearchFieldInContent: Boolean = true
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

    sealed interface Intent : BaseUiIntent {
        data class SetShowSearchFieldInContent(val show: Boolean) : Intent
        data class OnQueryChange(val query: String) : Intent
        object PerformSearch : Intent
        object ClearSearch : Intent
        object ClearError : Intent
        object Retry : Intent
        data class OnUserClick(val userId: String) : Intent
        data class OnHashtagClick(val hashtag: String) : Intent
        data class OnPostClick(val postId: String) : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToUser(val userId: String) : Effect
        data class NavigateToHashtag(val hashtag: String) : Effect
        data class NavigateToPost(val postId: String) : Effect
    }
}
