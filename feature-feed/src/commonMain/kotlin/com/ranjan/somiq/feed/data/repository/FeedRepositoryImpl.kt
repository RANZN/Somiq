package com.ranjan.somiq.feed.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.feed.data.model.CreatePostRequest
import com.ranjan.somiq.feed.data.model.CreateStoryRequest
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.feed.data.model.StoryResponse
import com.ranjan.somiq.feed.data.model.ToggleResponse
import com.ranjan.somiq.feed.data.model.UploadResponse
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class FeedRepositoryImpl(
    private val httpClient: HttpClient
) : FeedRepository {

    override suspend fun getFeedPage(after: String?, limit: Int): Result<PaginationResult<Post>> {
        val url = buildString {
            append("$BASE_URL/v1/posts?limit=$limit")
            if (!after.isNullOrBlank()) append("&after=$after")
        }
        return safeApiCall(
            apiCall = { httpClient.get(url) }
        )
    }

    override suspend fun getPostsByUser(userId: String): Result<List<Post>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts?authorId=$userId") },
            onSuccess = { response -> response.body<PaginationResult<Post>>().data }
        )
    }

    override suspend fun getBookmarkedPosts(): Result<List<Post>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts/bookmarks") },
            onSuccess = { response -> response.body<PaginationResult<Post>>().data }
        )
    }

    override suspend fun getPost(postId: String): Result<Post> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts/$postId") }
        )
    }

    override suspend fun getStories(): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories") },
            onSuccess = { response -> response.body<StoryResponse>().data }
        )
    }

    override suspend fun getMyStories(): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories/me") },
            onSuccess = { response -> response.body<List<Story>>() }
        )
    }

    override suspend fun getUserStories(userId: String): Result<List<Story>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories/user/$userId") },
            onSuccess = { response -> response.body<List<Story>>() }
        )
    }

    override suspend fun getStory(storyId: String): Result<Story> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/stories/$storyId") }
        )
    }

    override suspend fun createStory(request: CreateStoryRequest): Result<Story> {
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/stories") {
                    setBody(request)
                }
            }
        )
    }

    override suspend fun toggleLike(postId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/posts/$postId/like") }
        )
    }

    override suspend fun toggleBookmark(postId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/posts/$postId/bookmark") }
        )
    }

    override suspend fun createPost(request: CreatePostRequest): Result<Post> {
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/posts") {
                    setBody(request)
                }
            }
        )
    }

    override suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String> {
        return safeApiCall(
            apiCall = {
                httpClient.submitFormWithBinaryData(
                    url = "$BASE_URL/v1/media/upload",
                    formData = formData {
                        append(
                            key = "file",
                            value = imageBytes,
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            }
                        )
                    }
                )
            },
            onSuccess = { response -> response.body<UploadResponse>().url }
        )
    }
}
