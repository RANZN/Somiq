package com.ranjan.somiq.feed.domain.repository

import com.ranjan.somiq.feed.domain.model.CreateStoryRequest
import com.ranjan.somiq.feed.domain.model.Story

interface StoryRepository {
    suspend fun getStories(): Result<List<Story>>
    suspend fun getStory(storyId: String): Result<Story>
    suspend fun getMyStories(): Result<List<Story>>
    suspend fun getUserStories(userId: String): Result<List<Story>>
    suspend fun createStory(request: CreateStoryRequest): Result<Story>
}
