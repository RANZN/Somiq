package com.ranjan.somiq.search.data.model

import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.reels.data.model.Reel
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val bio: String? = null
)

@Serializable
data class SearchResult(
    val users: List<User>,
    val posts: List<Post>,
    val reels: List<Reel>
)
