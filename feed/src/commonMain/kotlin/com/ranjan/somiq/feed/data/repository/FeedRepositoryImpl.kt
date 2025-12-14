package com.ranjan.somiq.feed.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.feed.data.model.StoryResponse
import com.ranjan.somiq.feed.domain.repository.FeedRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode

class FeedRepositoryImpl(
    private val httpClient: HttpClient
) : FeedRepository {

    override suspend fun getFeed(): Result<List<Post>> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/posts")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val paginationResult = response.body<PaginationResult<Post>>()
                    Result.success(paginationResult.data)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse posts: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load feed: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load feed: ${e.message}"))
        }
    }

    override suspend fun getPost(postId: String): Result<Post> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/posts/$postId")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val post = response.body<Post>()
                    Result.success(post)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse post: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load post: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load post: ${e.message}"))
        }
    }

    override suspend fun getStories(): Result<List<Story>> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/stories")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val storiesResponse = response.body<StoryResponse>()
                    Result.success(storiesResponse.data)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse stories: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load stories: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load stories: ${e.message}"))
        }
    }

    override suspend fun toggleLike(postId: String): Result<Boolean> {
        return try {
            val response = httpClient.post("$BASE_URL/v1/posts/$postId/like")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleBookmark(postId: String): Result<Boolean> {
        return try {
            val response = httpClient.post("$BASE_URL/v1/posts/$postId/bookmark")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
