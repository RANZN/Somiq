package com.ranjan.somiq.app.home.data.repository

import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.app.home.data.model.NotificationResponse
import com.ranjan.somiq.app.home.domain.repository.NotificationRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put

class NotificationRepositoryImpl(
    private val httpClient: HttpClient
) : NotificationRepository {

    override suspend fun getNotifications(
        unreadOnly: Boolean,
        after: String?,
        limit: Int
    ): Result<List<NotificationResponse>> {
        val queryParams = buildString {
            append("unreadOnly=$unreadOnly")
            if (after != null) {
                append("&after=$after")
            }
            append("&limit=$limit")
        }
        
        return safeApiCall(
            apiCall = { httpClient.get("v1/notifications?$queryParams") },
            onSuccess = { response -> response.body<PaginationResult<NotificationResponse>>().data }
        )
    }

    override suspend fun getUnreadCount(): Result<Long> {
        return safeApiCall(
            apiCall = { httpClient.get("v1/notifications/unread-count") },
            onSuccess = { response ->
                @kotlinx.serialization.Serializable
                data class UnreadCountResponse(val unreadCount: Long)
                response.body<UnreadCountResponse>().unreadCount
            }
        )
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        return safeApiCall(
            apiCall = { httpClient.put("v1/notifications/$notificationId/read") }
        )
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return safeApiCall(
            apiCall = { httpClient.put("v1/notifications/read-all") }
        )
    }
}

