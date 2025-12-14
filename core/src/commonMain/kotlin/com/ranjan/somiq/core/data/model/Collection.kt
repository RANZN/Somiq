package com.ranjan.somiq.core.data.model

import com.ranjan.somiq.core.domain.model.ItemType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionResponse(
    @SerialName("collectionId")
    val id: String,
    val name: String,
    val description: String?,
    val itemsCount: Long,
    val createdAt: Long,
    val updatedAt: Long?
)

@Serializable
data class CollectionItemResponse(
    @SerialName("itemId")
    val id: String,
    @SerialName("collectionId")
    val collectionId: String,
    val itemType: ItemType,
    @SerialName("itemRefId")
    val itemRefId: String, // postId or reelId
    val addedAt: Long
)

