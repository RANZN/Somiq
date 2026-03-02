package com.ranjan.somiq.collections

import com.ranjan.somiq.core.data.model.CollectionResponse
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState

object CollectionsContract {
    data class UiState(
        val isLoading: Boolean = false,
        val collections: List<CollectionResponse> = emptyList(),
        val error: String? = null
    ) : BaseUiState

    sealed class Action : BaseUiAction {
        data object LoadCollections : Action()
        data class CreateCollection(val name: String, val description: String?) : Action()
        data object Refresh : Action()
    }

    sealed class Effect : BaseUiEffect {
        data class ShowError(val message: String) : Effect()
        data object CollectionCreated : Effect()
    }
}

