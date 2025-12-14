package com.ranjan.somiq.collections

import androidx.lifecycle.ViewModel
import com.ranjan.somiq.core.domain.repository.CollectionRepository
import com.ranjan.somiq.core.domain.usecase.GetCollectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CollectionsViewModel : ViewModel(), KoinComponent {
    private val getCollectionsUseCase: GetCollectionsUseCase by inject()
    private val collectionRepository: CollectionRepository by inject()

    private val _uiState = MutableStateFlow(CollectionsUiState())
    val uiState: StateFlow<CollectionsUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<CollectionsEvent?>(null)
    val events: StateFlow<CollectionsEvent?> = _events.asStateFlow()

    init {
        handleAction(CollectionsAction.LoadCollections)
    }

    fun handleAction(action: CollectionsAction) {
        when (action) {
            is CollectionsAction.LoadCollections -> loadCollections()
            is CollectionsAction.CreateCollection -> createCollection(action.name, action.description)
            is CollectionsAction.Refresh -> loadCollections()
        }
    }

    private fun loadCollections() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            getCollectionsUseCase().fold(
                onSuccess = { collections ->
                    _uiState.update {
                        it.copy(
                            collections = collections,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load collections"
                        )
                    }
                }
            )
        }
    }

    private fun createCollection(name: String, description: String?) {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            collectionRepository.createCollection(name, description).fold(
                onSuccess = {
                    _events.value = CollectionsEvent.CollectionCreated
                    loadCollections()
                },
                onFailure = { error ->
                    _events.value = CollectionsEvent.ShowError(
                        error.message ?: "Failed to create collection"
                    )
                }
            )
        }
    }

    fun clearEvent() {
        _events.value = null
    }
}

