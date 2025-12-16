package com.ranjan.somiq.search.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.search.domain.usecase.SearchUseCase
import com.ranjan.somiq.search.ui.SearchContract.Action
import com.ranjan.somiq.search.ui.SearchContract.Effect
import com.ranjan.somiq.search.ui.SearchContract.UiState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase
) : BaseViewModel<UiState, Action, Effect>(UiState()) {

    override fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.OnQueryChange -> {
                    setState { copy(searchQuery = action.query) }
                }
                is Action.PerformSearch -> {
                    performSearch()
                }
                is Action.ClearSearch -> {
                    setState { 
                        copy(
                            searchQuery = "",
                            isSearchActive = false,
                            searchResults = null
                        )
                    }
                }
                is Action.OnUserClick -> {
                    emitEffect(Effect.NavigateToUser(action.userId))
                }
                is Action.OnHashtagClick -> {
                    emitEffect(Effect.NavigateToHashtag(action.hashtag))
                }
                is Action.OnPostClick -> {
                    emitEffect(Effect.NavigateToPost(action.postId))
                }
                is Action.ClearError -> {
                    setState { copy(error = null) }
                }
                is Action.Retry -> {
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
