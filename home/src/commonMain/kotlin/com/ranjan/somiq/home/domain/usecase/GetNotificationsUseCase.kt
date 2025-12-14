package com.ranjan.somiq.home.domain.usecase

import com.ranjan.somiq.home.data.model.NotificationResponse
import com.ranjan.somiq.home.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        unreadOnly: Boolean = false,
        after: String? = null,
        limit: Int = 20
    ): Result<List<NotificationResponse>> {
        return notificationRepository.getNotifications(unreadOnly, after, limit)
    }
}

