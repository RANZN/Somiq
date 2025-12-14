package com.ranjan.somiq.notifications

import androidx.lifecycle.ViewModel
import com.ranjan.somiq.home.domain.repository.NotificationRepository
import com.ranjan.somiq.home.domain.usecase.GetNotificationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationsViewModel : ViewModel(), KoinComponent {
    private val getNotificationsUseCase: GetNotificationsUseCase by inject()
    private val notificationRepository: NotificationRepository by inject()

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<NotificationsEvent?>(null)
    val events: StateFlow<NotificationsEvent?> = _events.asStateFlow()

    init {
        handleAction(NotificationsAction.LoadNotifications)
        handleAction(NotificationsAction.LoadUnreadCount)
    }

    fun handleAction(action: NotificationsAction) {
        when (action) {
            is NotificationsAction.LoadNotifications -> loadNotifications()
            is NotificationsAction.LoadUnreadCount -> loadUnreadCount()
            is NotificationsAction.MarkAsRead -> markAsRead(action.notificationId)
            is NotificationsAction.MarkAllAsRead -> markAllAsRead()
            is NotificationsAction.Refresh -> {
                loadNotifications()
                loadUnreadCount()
            }
        }
    }

    private fun loadNotifications() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            getNotificationsUseCase().fold(
                onSuccess = { notifications ->
                    _uiState.update {
                        it.copy(
                            notifications = notifications,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load notifications"
                        )
                    }
                }
            )
        }
    }

    private fun loadUnreadCount() {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            notificationRepository.getUnreadCount().fold(
                onSuccess = { count ->
                    _uiState.update { it.copy(unreadCount = count) }
                },
                onFailure = { }
            )
        }
    }

    private fun markAsRead(notificationId: String) {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            notificationRepository.markAsRead(notificationId).fold(
                onSuccess = {
                    loadNotifications()
                    loadUnreadCount()
                },
                onFailure = { error ->
                    _events.value = NotificationsEvent.ShowError(
                        error.message ?: "Failed to mark as read"
                    )
                }
            )
        }
    }

    private fun markAllAsRead() {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default).launch {
            notificationRepository.markAllAsRead().fold(
                onSuccess = {
                    loadNotifications()
                    loadUnreadCount()
                },
                onFailure = { error ->
                    _events.value = NotificationsEvent.ShowError(
                        error.message ?: "Failed to mark all as read"
                    )
                }
            )
        }
    }

    fun clearEvent() {
        _events.value = null
    }
}

