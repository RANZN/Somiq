package com.ranjan.somiq.collections

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.collections.CollectionsContract.Action
import com.ranjan.somiq.collections.CollectionsContract.Effect
import com.ranjan.somiq.collections.CollectionsContract.UiState
import com.ranjan.somiq.core.domain.repository.CollectionRepository
import com.ranjan.somiq.core.domain.usecase.GetCollectionsUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CollectionsViewModel : BaseViewModel<UiState, Action, Effect>(UiState()), KoinComponent {
    private val getCollectionsUseCase: GetCollectionsUseCase by inject()
    private val collectionRepository: CollectionRepository by inject()

    init {
        handleAction(Action.LoadCollections)
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.LoadCollections -> loadCollections()
            is Action.CreateCollection -> createCollection(action.name, action.description)
            is Action.Refresh -> loadCollections()
        }
    }

    private fun loadCollections() {
        setState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getCollectionsUseCase().fold(
                onSuccess = { collections ->
                    setState {
                        copy(
                            collections = collections,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    setState {
                        copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load collections"
                        )
                    }
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
                    emitEffect(Effect.ShowError(
                        error.message ?: "Failed to create collection"
                    ))
                }
            )
        }
    }
}

