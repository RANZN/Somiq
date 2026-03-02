package com.ranjan.somiq.profile.data.model

import kotlinx.serialization.SerialName
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

@Serializable
data class User(
    @SerialName("userId")
    val id: String,
    val name: String,
    val email: String,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val bio: String? = null
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val profilePictureUrl: String? = null
)
