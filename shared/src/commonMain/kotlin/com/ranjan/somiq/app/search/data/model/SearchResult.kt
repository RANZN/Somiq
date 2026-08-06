package com.ranjan.somiq.app.search.data.model

import com.ranjan.somiq.feed.data.model.PostDto
import com.ranjan.somiq.reels.data.model.Reel
import kotlinx.serialization.Serializable

@Serializable
data class SearchUserDto(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val bio: String? = null
)

@Serializable
data class SearchResultResponse(
    val users: List<SearchUserDto>,
    val posts: List<PostDto>,
    val reels: List<Reel>
)
