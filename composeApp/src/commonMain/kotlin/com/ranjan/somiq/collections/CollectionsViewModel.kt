package com.ranjan.somiq.collections

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.collections.CollectionsContract.Intent
import com.ranjan.somiq.collections.CollectionsContract.Effect
import com.ranjan.somiq.collections.CollectionsContract.UiState
import com.ranjan.somiq.core.domain.repository.CollectionRepository
import com.ranjan.somiq.core.domain.usecase.GetCollectionsUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CollectionsViewModel : BaseViewModel<UiState, Intent, Effect>(UiState()), KoinComponent {
    private val getCollectionsUseCase: GetCollectionsUseCase by inject()
    private val collectionRepository: CollectionRepository by inject()

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

