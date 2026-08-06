package com.ranjan.somiq.feed.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ToggleResponse(
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Long,
    val bookmarksCount: Long
)
