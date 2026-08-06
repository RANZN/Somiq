package com.ranjan.somiq.feed.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ToggleResponse(
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Long,
    val bookmarksCount: Long
)
