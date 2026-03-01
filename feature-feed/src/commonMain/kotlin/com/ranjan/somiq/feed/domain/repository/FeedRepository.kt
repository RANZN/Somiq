package com.ranjan.somiq.feed.domain.repository

import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.feed.data.model.ToggleResponse

interface FeedRepository {
    suspend fun getFeed(): Result<List<Post>>
    suspend fun getPost(postId: String): Result<Post>
    suspend fun getStories(): Result<List<Story>>
    suspend fun createPost(request: CreatePostRequest): Result<Post>
    suspend fun toggleLike(postId: String): Result<ToggleResponse>
    suspend fun toggleBookmark(postId: String): Result<ToggleResponse>
}
