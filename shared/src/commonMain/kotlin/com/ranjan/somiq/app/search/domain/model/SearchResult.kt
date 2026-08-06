package com.ranjan.somiq.app.search.domain.model

import androidx.compose.runtime.Stable
import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.profile.domain.model.User
import com.ranjan.somiq.reels.data.model.Reel

@Stable
data class SearchResult(
    val users: List<User>,
    val posts: List<Post>,
    val reels: List<Reel>
)
