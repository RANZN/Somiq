package com.ranjan.somiq.feed.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.feed.data.model.StoryResponse
import com.ranjan.somiq.feed.data.model.ToggleResponse
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class FeedRepositoryImpl(
    private val httpClient: HttpClient
) : FeedRepository {

    override suspend fun getFeed(): Result<List<Post>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts") },
            onSuccess = { response -> response.body<PaginationResult<Post>>().data },
            errorMessage = "Failed to load feed"
        )
    }

    override suspend fun getPost(postId: String): Result<Post> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts/$postId") },
            errorMessage = "Failed to load post"
        )
    }

    override suspend fun getStories(): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories") },
            onSuccess = { response -> response.body<StoryResponse>().data },
            errorMessage = "Failed to load stories"
        )
    }

    override suspend fun toggleLike(postId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/posts/$postId/like") },
            errorMessage = "Failed to toggle like"
        )
    }

    override suspend fun toggleBookmark(postId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/posts/$postId/bookmark") },
            errorMessage = "Failed to toggle bookmark"
        )
    }

    override suspend fun createPost(request: CreatePostRequest): Result<Post> {
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/posts") {
                    setBody(request)
                }
            },
            errorMessage = "Failed to create post"
        )
    }
}
