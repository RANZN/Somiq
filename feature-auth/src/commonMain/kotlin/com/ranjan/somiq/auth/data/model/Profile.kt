package com.ranjan.somiq.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val user: User,
    val postsCount: Long = 0,
    val reelsCount: Long = 0,
    val followersCount: Long = 0,
    val followingCount: Long = 0,
    val isFollowing: Boolean = false
)

