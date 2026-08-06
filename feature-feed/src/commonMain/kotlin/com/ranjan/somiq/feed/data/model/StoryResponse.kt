package com.ranjan.somiq.feed.data.model

import com.ranjan.somiq.feed.domain.model.Story
import kotlinx.serialization.Serializable

@Serializable
data class StoryResponse(
    val data: List<Story>,
    val nextCursor: String? = null
)
