package com.ranjan.somiq.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _events = Channel<SearchEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    fun handleAction(action: SearchAction) {
        viewModelScope.launch {
            when (action) {
                is SearchAction.OnQueryChange -> {
                    _uiState.update { it.copy(searchQuery = action.query) }
                }
                is SearchAction.PerformSearch -> {
                    performSearch()
                }
                is SearchAction.ClearSearch -> {
                    _uiState.update { 
                        it.copy(
                            searchQuery = "",
                            isSearchActive = false
                        )
                    }
                }
                is SearchAction.OnUserClick -> {
                    _events.send(SearchEvent.NavigateToUser(action.userId))
                }
                is SearchAction.OnHashtagClick -> {
                    _events.send(SearchEvent.NavigateToHashtag(action.hashtag))
                }
                is SearchAction.OnPostClick -> {
                    _events.send(SearchEvent.NavigateToPost(action.postId))
                }
                is SearchAction.ClearError -> {
                    _uiState.update { it.copy(error = null) }
                }
                is SearchAction.Retry -> {
                    _uiState.update { it.copy(error = null) }
                    performSearch()
                }
            }
        }
    }

    private suspend fun performSearch() {
        val query = _uiState.value.searchQuery
        if (query.isBlank()) return

        _uiState.update { 
            it.copy(
                isLoading = true,
                error = null,
                isSearchActive = true
            )
        }
        // TODO: Implement search functionality
        _uiState.update { it.copy(isLoading = false) }
    }
}

