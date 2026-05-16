package com.ranjan.somiq.core.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.model.CollectionItemResponse
import com.ranjan.somiq.core.data.model.CollectionResponse
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.domain.model.ItemType
import com.ranjan.somiq.core.domain.repository.CollectionRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class CreateCollectionRequest(val name: String, val description: String? = null)

@Serializable
data class UpdateCollectionRequest(val name: String? = null, val description: String? = null)

@Serializable
data class AddItemRequest(val itemType: ItemType, @Serializable val itemRefId: String)

class CollectionRepositoryImpl(
    private val httpClient: HttpClient
) : CollectionRepository {

    override suspend fun getCollections(): Result<List<CollectionResponse>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/collections") }
        )
    }

    override suspend fun createCollection(name: String, description: String?): Result<CollectionResponse> {
        val request = CreateCollectionRequest(name, description)
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/collections") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            }
        )
    }

    override suspend fun updateCollection(
        collectionId: String,
        name: String?,
        description: String?
    ): Result<CollectionResponse> {
        val request = UpdateCollectionRequest(name, description)
        return safeApiCall(
            apiCall = {
                httpClient.put("$BASE_URL/v1/collections/$collectionId") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            }
        )
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return safeApiCall(
            apiCall = { httpClient.delete("$BASE_URL/v1/collections/$collectionId") }
        )
    }

    override suspend fun getCollectionItems(collectionId: String): Result<List<CollectionItemResponse>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/collections/$collectionId/items") }
        )
    }

    override suspend fun addItemToCollection(
        collectionId: String,
        itemType: ItemType,
        itemRefId: String
    ): Result<CollectionItemResponse> {
        val request = AddItemRequest(itemType, itemRefId)
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/collections/$collectionId/items") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            }
        )
    }

    override suspend fun removeItemFromCollection(collectionId: String, itemId: String): Result<Unit> {
        return safeApiCall(
            apiCall = { httpClient.delete("$BASE_URL/v1/collections/$collectionId/items/$itemId") }
        )
    }
}

