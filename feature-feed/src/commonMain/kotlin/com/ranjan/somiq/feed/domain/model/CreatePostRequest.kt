package com.ranjan.somiq.feed.domain.model

data class CreatePostRequest(
    val caption: String,
    val media: List<ByteArray> = emptyList()
)
