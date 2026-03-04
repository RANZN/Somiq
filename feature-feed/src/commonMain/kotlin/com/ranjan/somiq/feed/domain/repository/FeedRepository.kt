package com.ranjan.somiq.feed.domain.repository

import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.data.model.CreateStoryRequest
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.feed.data.model.ToggleResponse

interface FeedRepository {
    suspend fun getFeed(): Result<List<Post>>
    suspend fun getPostsByUser(userId: String): Result<List<Post>>
    suspend fun getBookmarkedPosts(): Result<List<Post>>
    suspend fun getPost(postId: String): Result<Post>
    suspend fun getStories(): Result<List<Story>>
    suspend fun getStory(storyId: String): Result<Story>
    suspend fun getMyStories(): Result<List<Story>>
    suspend fun getUserStories(userId: String): Result<List<Story>>
    suspend fun createPost(request: CreatePostRequest): Result<Post>
    suspend fun createStory(request: CreateStoryRequest): Result<Story>
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String>
    suspend fun toggleLike(postId: String): Result<ToggleResponse>
    suspend fun toggleBookmark(postId: String): Result<ToggleResponse>
}
