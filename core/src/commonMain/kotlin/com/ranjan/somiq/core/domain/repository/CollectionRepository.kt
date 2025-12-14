package com.ranjan.somiq.core.domain.repository

import com.ranjan.somiq.core.data.model.CollectionItemResponse
import com.ranjan.somiq.core.data.model.CollectionResponse

interface CollectionRepository {
    suspend fun getCollections(): Result<List<CollectionResponse>>
    
    suspend fun createCollection(name: String, description: String?): Result<CollectionResponse>
    
    suspend fun updateCollection(
        collectionId: String,
        name: String?,
        description: String?
    ): Result<CollectionResponse>
    
    suspend fun deleteCollection(collectionId: String): Result<Unit>
    
    suspend fun getCollectionItems(collectionId: String): Result<List<CollectionItemResponse>>
    
    suspend fun addItemToCollection(
        collectionId: String,
        itemType: com.ranjan.somiq.core.domain.model.ItemType,
        itemRefId: String
    ): Result<CollectionItemResponse>
    
    suspend fun removeItemFromCollection(collectionId: String, itemId: String): Result<Unit>
}

