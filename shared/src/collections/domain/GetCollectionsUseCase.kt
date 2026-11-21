package com.ranjan.somiq.collections.domain

import com.ranjan.somiq.collections.data.CollectionResponse

class GetCollectionsUseCase(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(): Result<List<CollectionResponse>> {
        return collectionRepository.getCollections()
    }
}

