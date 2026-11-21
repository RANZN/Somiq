package com.ranjan.somiq.collections

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.collections.CollectionsContract.Effect
import com.ranjan.somiq.collections.CollectionsContract.Intent
import com.ranjan.somiq.collections.CollectionsContract.UiState
import com.ranjan.somiq.collections.domain.CollectionRepository
import com.ranjan.somiq.collections.domain.GetCollectionsUseCase
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsViewModel(
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val collectionRepository: CollectionRepository
) : BaseViewModel<Intent, Effect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    init {
        handleIntent(Intent.LoadCollections)
    }
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
            is Intent.LoadCollections -> loadCollections()
            is Intent.CreateCollection -> createCollection(intent.name, intent.description)
            is Intent.Refresh -> loadCollections()
            }
        }
    }
    private fun loadCollections() {
        _uiState.update {it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getCollectionsUseCase().fold(
                onSuccess = { collections ->
                    _uiState.update {it.copy(
                            collections = collections,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    val appError = error.toAppError(CollectionsContract.ScreenError.LoadCollectionsFailed)
                    _uiState.update {it.copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
    private fun createCollection(name: String, description: String?) {
        viewModelScope.launch {
            collectionRepository.createCollection(name, description).fold(
                onSuccess = {
                    emitEffect(Effect.CollectionCreated)
                    loadCollections()
                },
                onFailure = { error ->
                    showSnackbar(
                        error.toAppError(CollectionsContract.ScreenError.CreateCollectionFailed),
                    )
                }
            )
        }
    }
}
