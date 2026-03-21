package com.ranjan.somiq.feed.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val title: String,
    val content: String,
    val mediaUrls: List<String> = emptyList()
)
