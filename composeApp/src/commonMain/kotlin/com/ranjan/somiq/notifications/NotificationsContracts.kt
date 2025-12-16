package com.ranjan.somiq.notifications

import com.ranjan.somiq.core.presentation.viewmodel.BaseUiAction
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.home.data.model.NotificationResponse

object NotificationsContract {
    data class UiState(
        val isLoading: Boolean = false,
        val notifications: List<NotificationResponse> = emptyList(),
        val unreadCount: Long = 0,
        val error: String? = null
    ) : BaseUiState

    sealed class Action : BaseUiAction {
        data object LoadNotifications : Action()
        data object LoadUnreadCount : Action()
        data class MarkAsRead(val notificationId: String) : Action()
        data object MarkAllAsRead : Action()
        data object Refresh : Action()
    }

    sealed class Effect : BaseUiEffect {
        data class ShowError(val message: String) : Effect()
    }
}

