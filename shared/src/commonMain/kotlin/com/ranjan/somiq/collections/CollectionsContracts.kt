package com.ranjan.somiq.collections

import com.ranjan.somiq.collections.data.CollectionResponse
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_create_collection
import com.ranjan.somiq.core.resources.error_failed_to_load_collections

object CollectionsContract {
    sealed class ScreenError : BaseScreenError {
        data object LoadCollectionsFailed : ScreenError()
        data object CreateCollectionFailed : ScreenError()

        override fun toUiText(): UiText = when (this) {
            LoadCollectionsFailed -> UiText.Resource(Res.string.error_failed_to_load_collections)
            CreateCollectionFailed -> UiText.Resource(Res.string.error_failed_to_create_collection)
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val collections: List<CollectionResponse> = emptyList(),
        val error: AppError? = null
    ) : BaseUiState

    sealed class Intent : BaseUiIntent {
        data object LoadCollections : Intent()
        data class CreateCollection(val name: String, val description: String?) : Intent()
        data object Refresh : Intent()
    }

    sealed class Effect : BaseUiEffect {
        data object CollectionCreated : Effect()
    }
}
