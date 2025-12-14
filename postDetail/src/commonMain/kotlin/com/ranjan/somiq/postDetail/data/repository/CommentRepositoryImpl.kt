package com.ranjan.somiq.postDetail.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.postDetail.data.model.CommentResponse
import com.ranjan.somiq.postDetail.data.model.CreateCommentRequest
import com.ranjan.somiq.postDetail.data.model.UpdateCommentRequest
import com.ranjan.somiq.postDetail.domain.repository.CommentRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class CommentRepositoryImpl(
    private val httpClient: HttpClient
) : CommentRepository {

    override suspend fun getComments(
        postId: String?,
        reelId: String?,
        parentCommentId: String?,
        after: String?,
        limit: Int
    ): Result<List<CommentResponse>> {
        return try {
            val queryParams = buildString {
                if (postId != null) append("postId=$postId")
                if (reelId != null) {
                    if (isNotEmpty()) append("&")
                    append("reelId=$reelId")
                }
                if (parentCommentId != null) {
                    if (isNotEmpty()) append("&")
                    append("parentCommentId=$parentCommentId")
                }
                if (after != null) {
                    if (isNotEmpty()) append("&")
                    append("after=$after")
                }
                if (isNotEmpty()) append("&")
                append("limit=$limit")
            }
            
            val response = httpClient.get("$BASE_URL/v1/comments?$queryParams")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val paginationResult = response.body<PaginationResult<CommentResponse>>()
                    Result.success(paginationResult.data)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse comments: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load comments: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load comments: ${e.message}"))
        }
    }

    override suspend fun createComment(
        postId: String?,
        reelId: String?,
        content: String,
        parentCommentId: String?
    ): Result<CommentResponse> {
        return try {
            val queryParams = buildString {
                if (postId != null) append("postId=$postId")
                if (reelId != null) {
                    if (isNotEmpty()) append("&")
                    append("reelId=$reelId")
                }
            }
            
            val request = CreateCommentRequest(content, parentCommentId)
            val response = httpClient.post("$BASE_URL/v1/comments?$queryParams") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (response.status == HttpStatusCode.Created) {
                try {
                    val comment = response.body<CommentResponse>()
                    Result.success(comment)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse comment: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to create comment: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create comment: ${e.message}"))
        }
    }

    override suspend fun updateComment(commentId: String, content: String): Result<CommentResponse> {
        return try {
            val request = UpdateCommentRequest(content)
            val response = httpClient.put("$BASE_URL/v1/comments/$commentId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            if (response.status == HttpStatusCode.OK) {
                try {
                    val comment = response.body<CommentResponse>()
                    Result.success(comment)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse comment: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to update comment: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update comment: ${e.message}"))
        }
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> {
        return try {
            val response = httpClient.delete("$BASE_URL/v1/comments/$commentId")
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete comment: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete comment: ${e.message}"))
        }
    }

    override suspend fun toggleLike(commentId: String): Result<Boolean> {
        return try {
            val response = httpClient.post("$BASE_URL/v1/comments/$commentId/like")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
