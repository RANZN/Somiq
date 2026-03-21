package com.ranjan.somiq.core.domain.usecase

import com.ranjan.somiq.core.data.model.CollectionResponse
import com.ranjan.somiq.core.domain.repository.CollectionRepository

class GetCollectionsUseCase(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(): Result<List<CollectionResponse>> {
        return collectionRepository.getCollections()
    }
}

