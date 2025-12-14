package com.ranjan.somiq.notifications

import com.ranjan.somiq.home.data.model.NotificationResponse

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationResponse> = emptyList(),
    val unreadCount: Long = 0,
    val error: String? = null
)

sealed class NotificationsAction {
    data object LoadNotifications : NotificationsAction()
    data object LoadUnreadCount : NotificationsAction()
    data class MarkAsRead(val notificationId: String) : NotificationsAction()
    data object MarkAllAsRead : NotificationsAction()
    data object Refresh : NotificationsAction()
}

sealed class NotificationsEvent {
    data class ShowError(val message: String) : NotificationsEvent()
}

