package com.ranjan.somiq.home.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.core.domain.common.model.PaginationResult
import com.ranjan.somiq.home.data.model.NotificationResponse
import com.ranjan.somiq.home.domain.repository.NotificationRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.http.HttpStatusCode

class NotificationRepositoryImpl(
    private val httpClient: HttpClient
) : NotificationRepository {

    override suspend fun getNotifications(
        unreadOnly: Boolean,
        after: String?,
        limit: Int
    ): Result<List<NotificationResponse>> {
        return try {
            val queryParams = buildString {
                append("unreadOnly=$unreadOnly")
                if (after != null) {
                    append("&after=$after")
                }
                append("&limit=$limit")
            }
            
            val response = httpClient.get("$BASE_URL/v1/notifications?$queryParams")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val paginationResult = response.body<PaginationResult<NotificationResponse>>()
                    Result.success(paginationResult.data)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse notifications: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to load notifications: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load notifications: ${e.message}"))
        }
    }

    override suspend fun getUnreadCount(): Result<Long> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/notifications/unread-count")
            if (response.status == HttpStatusCode.OK) {
                try {
                    @kotlinx.serialization.Serializable
                    data class UnreadCountResponse(val unreadCount: Long)
                    val result = response.body<UnreadCountResponse>()
                    Result.success(result.unreadCount)
                } catch (e: Exception) {
                    Result.failure(Exception("Failed to parse unread count: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to get unread count: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get unread count: ${e.message}"))
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        return try {
            val response = httpClient.put("$BASE_URL/v1/notifications/$notificationId/read")
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark notification as read: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to mark notification as read: ${e.message}"))
        }
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return try {
            val response = httpClient.put("$BASE_URL/v1/notifications/read-all")
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark all notifications as read: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to mark all notifications as read: ${e.message}"))
        }
    }
}

