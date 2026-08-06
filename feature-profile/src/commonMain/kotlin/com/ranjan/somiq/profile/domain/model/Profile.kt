package com.ranjan.somiq.profile.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ProfileResponse(
    val user: User,
    val postsCount: Long = 0,
    val reelsCount: Long = 0,
    val followersCount: Long = 0,
    val followingCount: Long = 0,
    val isFollowing: Boolean = false
)

@Stable
data class User(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val bio: String? = null
)

data class UpdateProfileRequest(
    val name: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val profilePictureUrl: String? = null
)
