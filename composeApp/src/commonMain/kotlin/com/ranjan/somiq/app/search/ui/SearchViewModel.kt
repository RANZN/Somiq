package com.ranjan.somiq.app.search.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.app.search.domain.usecase.SearchUseCase
import com.ranjan.somiq.app.search.ui.SearchContract.Intent
import com.ranjan.somiq.app.search.ui.SearchContract.Effect
import com.ranjan.somiq.app.search.ui.SearchContract.UiState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase
) : BaseViewModel<UiState, Intent, Effect>(UiState()) {

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.SetShowSearchFieldInContent -> setState { copy(showSearchFieldInContent = intent.show) }
                is Intent.OnQueryChange -> {
                    setState { copy(searchQuery = intent.query) }
                }
                is Intent.PerformSearch -> {
                    performSearch()
                }
                is Intent.ClearSearch -> {
                    setState { 
                        copy(
                            searchQuery = "",
                            isSearchActive = false,
                            searchResults = null
                        )
                    }
                }
                is Intent.OnUserClick -> {
                    emitEffect(Effect.NavigateToUser(intent.userId))
                }
                is Intent.OnHashtagClick -> {
                    emitEffect(Effect.NavigateToHashtag(intent.hashtag))
                }
                is Intent.OnPostClick -> {
                    emitEffect(Effect.NavigateToPost(intent.postId))
                }
                is Intent.ClearError -> {
                    setState { copy(error = null) }
                }
                is Intent.Retry -> {
                    setState { copy(error = null) }
                    performSearch()
                }
            }
        }
    }

    private suspend fun performSearch() {
        val query = state.value.searchQuery
        if (query.isBlank()) return

        setState { 
            copy(
                isLoading = true,
                error = null,
                isSearchActive = true
            )
        }
        
        searchUseCase(query).getOrElse { error ->
            setState {
                copy(
                    isLoading = false,
                    error = error.message ?: "Failed to search"
                )
            }
            return
        }.let { results ->
            setState {
                copy(
                    searchResults = results,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}
