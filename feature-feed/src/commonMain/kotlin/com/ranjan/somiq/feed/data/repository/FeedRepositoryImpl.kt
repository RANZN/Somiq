package com.ranjan.somiq.feed.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.feed.data.model.*
import com.ranjan.somiq.feed.data.mapper.*
import com.ranjan.somiq.feed.domain.model.CreatePostRequest
import com.ranjan.somiq.feed.domain.model.Post
import com.ranjan.somiq.feed.domain.model.ToggleResponse
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import com.ranjan.somiq.feed.data.cache.InMemoryPostCache
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.flow.StateFlow
import com.ranjan.somiq.core.util.currentTimeMillis

class FeedRepositoryImpl(
    private val httpClient: HttpClient,
    private val cache: InMemoryPostCache
) : FeedRepository {

    override val myPostsFlow: StateFlow<List<Post>> = cache.myPostsFlow
    override val bookmarkedPostsFlow: StateFlow<List<Post>> = cache.bookmarkedPostsFlow

    override suspend fun getFeedPage(after: String?, limit: Int): Result<PaginationResult<Post>> {
        val url = buildString {
            append("$BASE_URL/v1/posts?limit=$limit")
            if (!after.isNullOrBlank()) append("&after=$after")
        }
        return safeApiCall(
            apiCall = { httpClient.get(url) },
            onSuccess = { response ->
                val dtoResult = response.body<PaginationResult<PostDto>>()
                val posts = dtoResult.data.map { it.toDomain() }
                cache.cachePosts(posts)
                PaginationResult(
                    data = posts,
                    nextCursor = dtoResult.nextCursor
                )
            }
        )
    }

    override suspend fun getPostsByUser(userId: String): Result<List<Post>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts?authorId=$userId") },
            onSuccess = { response ->
                val posts = response.body<PaginationResult<PostDto>>().data.map { it.toDomain() }
                cache.cachePosts(posts)
                cache.setMyPosts(posts)
                posts
            }
        )
    }

    override suspend fun getBookmarkedPosts(): Result<List<Post>> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts/bookmarks") },
            onSuccess = { response ->
                val posts = response.body<PaginationResult<PostDto>>().data.map { it.toDomain() }
                cache.cachePosts(posts)
                cache.setBookmarkedPosts(posts)
                posts
            }
        )
    }

    override suspend fun getPost(postId: String): Result<Post> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/posts/$postId") },
            onSuccess = { response ->
                val post = response.body<PostDto>().toDomain()
                cache.cachePost(post)
                post
            }
        )
    }

    override suspend fun toggleLike(postId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/posts/$postId/like") },
            onSuccess = { response ->
                val toggleResult = response.body<ToggleResponseDto>().toDomain()
                cache.updateLikeStatus(postId, toggleResult.isLiked, toggleResult.likesCount)
                toggleResult
            }
        )
    }

    override suspend fun toggleBookmark(postId: String): Result<ToggleResponse> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/posts/$postId/bookmark") },
            onSuccess = { response ->
                val toggleResult = response.body<ToggleResponseDto>().toDomain()
                cache.updateBookmarkStatus(postId, toggleResult.isBookmarked, toggleResult.bookmarksCount)
                toggleResult
            }
        )
    }

    override suspend fun createPost(request: CreatePostRequest): Result<Post> {
        return safeApiCall(
            apiCall = {
                if (request.media.isNotEmpty()) {
                    httpClient.submitFormWithBinaryData(
                        url = "$BASE_URL/v1/posts",
                        formData = formData {
                            append(key = "caption", value = request.caption)

                            // Append all media files
                            request.media.forEachIndexed { index, byte ->
                                val fileName = "media_${index}_${currentTimeMillis()}.jpg"
                                append(
                                    key = fileName,
                                    value = byte,
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentType, "image/jpeg")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=\"$fileName\""
                                        )
                                    }
                                )
                            }
                        }
                    )
                } else {
                    httpClient.post("$BASE_URL/v1/posts") {
                        contentType(ContentType.Application.Json)
                        setBody(request.toDto())
                    }
                }
            },
            onSuccess = { response ->
                val createdPost = response.body<PostDto>().toDomain()
                cache.addPostToMyPosts(createdPost)
                createdPost
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
