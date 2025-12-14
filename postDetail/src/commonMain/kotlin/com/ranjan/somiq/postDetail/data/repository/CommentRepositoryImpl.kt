package com.ranjan.somiq.postDetail.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.data.network.safeApiCallUnit
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
        
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/comments?$queryParams") },
            onSuccess = { response -> response.body<PaginationResult<CommentResponse>>().data },
            errorMessage = "Failed to load comments"
        )
    }

    override suspend fun createComment(
        postId: String?,
        reelId: String?,
        content: String,
        parentCommentId: String?
    ): Result<CommentResponse> {
        val queryParams = buildString {
            if (postId != null) append("postId=$postId")
            if (reelId != null) {
                if (isNotEmpty()) append("&")
                append("reelId=$reelId")
            }
        }
        
        val request = CreateCommentRequest(content, parentCommentId)
        return safeApiCall(
            apiCall = {
                httpClient.post("$BASE_URL/v1/comments?$queryParams") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            },
            errorMessage = "Failed to create comment"
        )
    }

    override suspend fun updateComment(commentId: String, content: String): Result<CommentResponse> {
        val request = UpdateCommentRequest(content)
        return safeApiCall(
            apiCall = {
                httpClient.put("$BASE_URL/v1/comments/$commentId") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            },
            errorMessage = "Failed to update comment"
        )
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> {
        return safeApiCallUnit(
            apiCall = { httpClient.delete("$BASE_URL/v1/comments/$commentId") },
            errorMessage = "Failed to delete comment"
        )
    }

    override suspend fun toggleLike(commentId: String): Result<Boolean> {
        return safeApiCall(
            apiCall = { httpClient.post("$BASE_URL/v1/comments/$commentId/like") },
            onSuccess = { response -> response.status.value == 200 },
            errorMessage = "Failed to toggle like"
        )
    }
}
