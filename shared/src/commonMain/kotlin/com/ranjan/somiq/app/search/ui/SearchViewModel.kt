package com.ranjan.somiq.app.search.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.app.search.domain.usecase.SearchUseCase
import com.ranjan.somiq.app.search.ui.SearchContract.Effect
import com.ranjan.somiq.app.search.ui.SearchContract.Intent
import com.ranjan.somiq.app.search.ui.SearchContract.UiState
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.SetShowSearchFieldInContent -> _uiState.update {it.copy(showSearchFieldInContent = intent.show) }
                is Intent.OnQueryChange -> {
                    _uiState.update {it.copy(searchQuery = intent.query) }
                }
                is Intent.PerformSearch -> {
                    performSearch()
                }
                is Intent.ClearSearch -> {
                    _uiState.update {it.copy(
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
                    _uiState.update {it.copy(error = null) }
                }
                is Intent.Retry -> {
                    _uiState.update {it.copy(error = null) }
                    performSearch()
                }
            }
        }
    }
    private suspend fun performSearch() {
        val query = uiState.value.searchQuery
        if (query.isBlank()) return
        _uiState.update {it.copy(
                isLoading = true,
                error = null,
                isSearchActive = true
            )
        }
        searchUseCase(query).getOrElse { error ->
            val appError = error.toAppError(SearchContract.ScreenError.SearchFailed)
            _uiState.update {it.copy(isLoading = false, error = appError) }
            showSnackbar(appError)
            return
        }.let { results ->
            _uiState.update {it.copy(
                    searchResults = results,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}
