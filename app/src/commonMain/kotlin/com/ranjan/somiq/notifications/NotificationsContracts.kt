package com.ranjan.somiq.notifications

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.app.home.data.model.NotificationResponse

object NotificationsContract {
    data class UiState(
        val isLoading: Boolean = false,
        val notifications: List<NotificationResponse> = emptyList(),
        val unreadCount: Long = 0,
        val error: String? = null
    ) : BaseUiState

    sealed class Intent : BaseUiIntent {
        data object LoadNotifications : Intent()
        data object LoadUnreadCount : Intent()
        data class MarkAsRead(val notificationId: String) : Intent()
        data object MarkAllAsRead : Intent()
        data object Refresh : Intent()
    }

    sealed class Effect : BaseUiEffect {
        data class ShowError(val message: String) : Effect()
    }
}

