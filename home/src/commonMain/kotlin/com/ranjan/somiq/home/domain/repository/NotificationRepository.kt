package com.ranjan.somiq.home.domain.repository

import com.ranjan.somiq.home.data.model.NotificationResponse

interface NotificationRepository {
    suspend fun getNotifications(
        unreadOnly: Boolean = false,
        after: String? = null,
        limit: Int = 20
    ): Result<List<NotificationResponse>>
    
    suspend fun getUnreadCount(): Result<Long>
    
    suspend fun markAsRead(notificationId: String): Result<Unit>
    
    suspend fun markAllAsRead(): Result<Unit>
}

