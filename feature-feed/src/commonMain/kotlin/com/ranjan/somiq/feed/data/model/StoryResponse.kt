package com.ranjan.somiq.feed.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StoryResponse(
    val data: List<StoryDto>,
    val nextCursor: String? = null
)
