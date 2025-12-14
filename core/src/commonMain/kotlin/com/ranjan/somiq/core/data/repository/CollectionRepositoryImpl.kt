package com.ranjan.somiq.core.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.model.CollectionItemResponse
import com.ranjan.somiq.core.data.model.CollectionResponse
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.domain.model.ItemType
import com.ranjan.somiq.core.domain.repository.CollectionRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
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
        return try {
            val response = httpClient.get("$BASE_URL/v1/collections")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val collections = response.body<List<CollectionResponse>>()
                    Result.success(collections)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse collections: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load collections: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load collections: ${e.message}"))
        }
    }

    override suspend fun createCollection(name: String, description: String?): Result<CollectionResponse> {
        return try {
            val request = CreateCollectionRequest(name, description)
            val response = httpClient.post("$BASE_URL/v1/collections") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (response.status == HttpStatusCode.Created) {
                try {
                    val collection = response.body<CollectionResponse>()
                    Result.success(collection)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse collection: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to create collection: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create collection: ${e.message}"))
        }
    }

    override suspend fun updateCollection(
        collectionId: String,
        name: String?,
        description: String?
    ): Result<CollectionResponse> {
        return try {
            val request = UpdateCollectionRequest(name, description)
            val response = httpClient.put("$BASE_URL/v1/collections/$collectionId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (response.status == HttpStatusCode.OK) {
                try {
                    val collection = response.body<CollectionResponse>()
                    Result.success(collection)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse collection: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to update collection: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update collection: ${e.message}"))
        }
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return try {
            val response = httpClient.delete("$BASE_URL/v1/collections/$collectionId")
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete collection: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete collection: ${e.message}"))
        }
    }

    override suspend fun getCollectionItems(collectionId: String): Result<List<CollectionItemResponse>> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/collections/$collectionId/items")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val items = response.body<List<CollectionItemResponse>>()
                    Result.success(items)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse collection items: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load collection items: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load collection items: ${e.message}"))
        }
    }

    override suspend fun addItemToCollection(
        collectionId: String,
        itemType: ItemType,
        itemRefId: String
    ): Result<CollectionItemResponse> {
        return try {
            val request = AddItemRequest(itemType, itemRefId)
            val response = httpClient.post("$BASE_URL/v1/collections/$collectionId/items") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (response.status == HttpStatusCode.Created) {
                try {
                    val item = response.body<CollectionItemResponse>()
                    Result.success(item)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse collection item: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to add item to collection: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to add item to collection: ${e.message}"))
        }
    }

    override suspend fun removeItemFromCollection(collectionId: String, itemId: String): Result<Unit> {
        return try {
            val response = httpClient.delete("$BASE_URL/v1/collections/$collectionId/items/$itemId")
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to remove item from collection: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to remove item from collection: ${e.message}"))
        }
    }
}

