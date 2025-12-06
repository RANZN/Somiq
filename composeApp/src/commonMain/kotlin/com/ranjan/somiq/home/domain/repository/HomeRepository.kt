package com.ranjan.somiq.home.domain.repository

import com.ranjan.somiq.home.data.model.Post
import com.ranjan.somiq.home.data.model.Story

interface HomeRepository {
    suspend fun getFeed(): Result<List<Post>>
    suspend fun getPost(postId: String): Result<Post>
    suspend fun getStories(): Result<List<Story>>
    suspend fun toggleLike(postId: String): Result<Boolean>
    suspend fun toggleBookmark(postId: String): Result<Boolean>
}

