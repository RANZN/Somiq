package com.ranjan.somiq.notifications

import com.ranjan.somiq.app.home.data.model.NotificationResponse
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.error_failed_to_load_notifications
import com.ranjan.somiq.core.resources.error_failed_to_mark_all_notifications_read
import com.ranjan.somiq.core.resources.error_failed_to_mark_notification_read

object NotificationsContract {
    sealed class ScreenError : BaseScreenError {
        data object LoadNotificationsFailed : ScreenError()
        data object MarkNotificationReadFailed : ScreenError()
        data object MarkAllNotificationsReadFailed : ScreenError()
        override fun toUiText(): UiText = when (this) {
            LoadNotificationsFailed -> UiText.Resource(Res.string.error_failed_to_load_notifications)
            MarkNotificationReadFailed -> UiText.Resource(Res.string.error_failed_to_mark_notification_read)
            MarkAllNotificationsReadFailed ->
                UiText.Resource(Res.string.error_failed_to_mark_all_notifications_read)
        }
    }
    data class UiState(
        val isLoading: Boolean = false,
        val notifications: List<NotificationResponse> = emptyList(),
        val unreadCount: Long = 0,
        val error: AppError? = null
    )sealed class Intent : BaseUiIntent {
        data object LoadNotifications : Intent()
        data object LoadUnreadCount : Intent()
        data class MarkAsRead(val notificationId: String) : Intent()
        data object MarkAllAsRead : Intent()
        data object Refresh : Intent()
    }
}
