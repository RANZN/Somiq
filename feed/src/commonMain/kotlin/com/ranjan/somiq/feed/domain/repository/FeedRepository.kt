package com.ranjan.somiq.feed.domain.repository

import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story

interface FeedRepository {
    suspend fun getFeed(): Result<List<Post>>
    suspend fun getPost(postId: String): Result<Post>
    suspend fun getStories(): Result<List<Story>>
    suspend fun toggleLike(postId: String): Result<Boolean>
    suspend fun toggleBookmark(postId: String): Result<Boolean>
}
