package com.ranjan.somiq.app.search.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.app.search.data.model.SearchResult
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_search

object SearchContract {
    sealed class ScreenError : BaseScreenError {
        data object SearchFailed : ScreenError()

        override fun toUiText(): UiText? = when (this) {
            SearchFailed -> UiText.Resource(Res.string.error_failed_to_search)
        }
    }

    @Stable
    data class UiState(
        val searchQuery: String = "",
        val searchResults: SearchResult? = null,
        val isLoading: Boolean = false,
        val error: AppError? = null,
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
