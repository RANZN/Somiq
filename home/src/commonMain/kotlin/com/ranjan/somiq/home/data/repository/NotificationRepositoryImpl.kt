package com.ranjan.somiq.home.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.core.data.network.safeApiCallUnit
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.home.data.model.NotificationResponse
import com.ranjan.somiq.home.domain.repository.NotificationRepository
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
            apiCall = { httpClient.get("$BASE_URL/v1/notifications?$queryParams") },
            onSuccess = { response -> response.body<PaginationResult<NotificationResponse>>().data },
            errorMessage = "Failed to load notifications"
        )
    }

    override suspend fun getUnreadCount(): Result<Long> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/notifications/unread-count") },
            onSuccess = { response ->
                @kotlinx.serialization.Serializable
                data class UnreadCountResponse(val unreadCount: Long)
                response.body<UnreadCountResponse>().unreadCount
            },
            errorMessage = "Failed to get unread count"
        )
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        return safeApiCallUnit(
            apiCall = { httpClient.put("$BASE_URL/v1/notifications/$notificationId/read") },
            errorMessage = "Failed to mark notification as read"
        )
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return safeApiCallUnit(
            apiCall = { httpClient.put("$BASE_URL/v1/notifications/read-all") },
            errorMessage = "Failed to mark all notifications as read"
        )
    }
}

