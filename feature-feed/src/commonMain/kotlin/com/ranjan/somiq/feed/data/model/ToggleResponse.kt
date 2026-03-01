package com.ranjan.somiq.feed.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ToggleResponse(
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Long,
    val bookmarksCount: Long
)
