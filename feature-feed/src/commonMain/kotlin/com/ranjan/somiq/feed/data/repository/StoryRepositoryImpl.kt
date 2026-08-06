package com.ranjan.somiq.feed.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.feed.data.model.StoryDto
import com.ranjan.somiq.feed.data.model.StoryResponse
import com.ranjan.somiq.feed.data.mapper.toDomain
import com.ranjan.somiq.feed.data.mapper.toDto
import com.ranjan.somiq.feed.domain.model.CreateStoryRequest
import com.ranjan.somiq.feed.domain.model.Story
import com.ranjan.somiq.feed.domain.repository.StoryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class StoryRepositoryImpl(
    private val httpClient: HttpClient
) : StoryRepository {

    override suspend fun getStories(): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories") },
            onSuccess = { response ->
                response.body<StoryResponse>().data.map { it.toDomain() }
            }
        )
    }

    override suspend fun getMyStories(): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories/me") },
            onSuccess = { response ->
                response.body<List<StoryDto>>().map { it.toDomain() }
            }
        )
    }

    override suspend fun getUserStories(userId: String): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories/user/$userId") },
            onSuccess = { response ->
                response.body<List<StoryDto>>().map { it.toDomain() }
            }
        )
    }

    override suspend fun getStory(storyId: String): Result<Story> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories/$storyId") },
            onSuccess = { response ->
                response.body<StoryDto>().toDomain()
            }
        )
    }

    override suspend fun createStory(request: CreateStoryRequest): Result<Story> {
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/stories") {
                    contentType(ContentType.Application.Json)
                    setBody(request.toDto())
                }
            },
            onSuccess = { response ->
                response.body<StoryDto>().toDomain()
            }
        )
    }
}
