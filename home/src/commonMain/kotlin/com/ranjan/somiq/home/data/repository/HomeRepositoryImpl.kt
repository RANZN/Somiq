package com.ranjan.somiq.home.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.home.data.model.Post
import com.ranjan.somiq.home.data.model.PostResponse
import com.ranjan.somiq.home.data.model.Story
import com.ranjan.somiq.home.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode

class HomeRepositoryImpl(
    private val httpClient: HttpClient // Use auth HttpClient from DI
) : HomeRepository {

    override suspend fun getFeed(): Result<List<Post>> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/posts")
            if (response.status == HttpStatusCode.OK) {
                val posts = response.body<PostResponse>()
                Result.success(posts.data)
            } else {
                Result.failure(Exception("Failed to load feed"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPost(postId: String): Result<Post> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/posts/$postId")
            if (response.status == HttpStatusCode.OK) {
                val post = response.body<Post>()
                Result.success(post)
            } else {
                Result.failure(Exception("Failed to load post"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStories(): Result<List<Story>> {
        return try {
            val response = httpClient.get("$BASE_URL/stories")
            if (response.status == HttpStatusCode.OK) {
                val stories = response.body<List<Story>>()
                Result.success(stories)
            } else {
                Result.failure(Exception("Failed to load stories"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
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

