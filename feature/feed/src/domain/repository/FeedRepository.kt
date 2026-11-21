package com.ranjan.somiq.feed.domain.repository

import com.ranjan.somiq.feed.domain.model.CreatePostRequest
import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.feed.domain.model.ToggleResponse

import kotlinx.coroutines.flow.StateFlow

interface FeedRepository {
    val myPostsFlow: StateFlow<List<Post>>
    val bookmarkedPostsFlow: StateFlow<List<Post>>

    suspend fun getFeedPage(after: String? = null, limit: Int = 20): Result<PaginationResult<Post>>
    suspend fun getPostsByUser(userId: String): Result<List<Post>>
    suspend fun getBookmarkedPosts(): Result<List<Post>>
    suspend fun getPost(postId: String): Result<Post>
    suspend fun createPost(request: CreatePostRequest): Result<Post>
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String>
    suspend fun toggleLike(postId: String): Result<ToggleResponse>
    suspend fun toggleBookmark(postId: String): Result<ToggleResponse>
}
