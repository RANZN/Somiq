package com.ranjan.somiq.collections

import com.ranjan.somiq.core.data.model.CollectionResponse

data class CollectionsUiState(
    val isLoading: Boolean = false,
    val collections: List<CollectionResponse> = emptyList(),
    val error: String? = null
)

sealed class CollectionsAction {
    data object LoadCollections : CollectionsAction()
    data class CreateCollection(val name: String, val description: String?) : CollectionsAction()
    data object Refresh : CollectionsAction()
}

sealed class CollectionsEvent {
    data class ShowError(val message: String) : CollectionsEvent()
    data object CollectionCreated : CollectionsEvent()
}

