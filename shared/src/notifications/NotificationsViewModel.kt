package com.ranjan.somiq.notifications

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.app.home.domain.repository.NotificationRepository
import com.ranjan.somiq.app.home.domain.usecase.GetNotificationsUseCase
import com.ranjan.somiq.core.presentation.error.toAppError
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.notifications.NotificationsContract.Intent
import com.ranjan.somiq.notifications.NotificationsContract.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val notificationRepository: NotificationRepository
) : BaseViewModel<Intent, BaseUiEffect>() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    init {
        handleIntent(Intent.LoadNotifications)
        handleIntent(Intent.LoadUnreadCount)
    }
    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
            is Intent.LoadNotifications -> loadNotifications()
            is Intent.LoadUnreadCount -> loadUnreadCount()
            is Intent.MarkAsRead -> markAsRead(intent.notificationId)
            is Intent.MarkAllAsRead -> markAllAsRead()
            is Intent.Refresh -> {
                loadNotifications()
                loadUnreadCount()
            }
            }
        }
    }
    private fun loadNotifications() {
        _uiState.update {it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getNotificationsUseCase().fold(
                onSuccess = { notifications ->
                    _uiState.update {it.copy(
                            notifications = notifications,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    val appError = error.toAppError(NotificationsContract.ScreenError.LoadNotificationsFailed)
                    _uiState.update {it.copy(isLoading = false, error = appError) }
                    showSnackbar(appError)
                }
            )
        }
    }
    private fun loadUnreadCount() {
        viewModelScope.launch {
            notificationRepository.getUnreadCount().fold(
                onSuccess = { count ->
                    _uiState.update {it.copy(unreadCount = count) }
                },
                onFailure = { }
            )
        }
    }
    private fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId).fold(
                onSuccess = {
                    loadNotifications()
                    loadUnreadCount()
                },
                onFailure = { error ->
                    showSnackbar(
                        error.toAppError(NotificationsContract.ScreenError.MarkNotificationReadFailed),
                    )
                }
            )
        }
    }
    private fun markAllAsRead() {
        viewModelScope.launch {
            notificationRepository.markAllAsRead().fold(
                onSuccess = {
                    loadNotifications()
                    loadUnreadCount()
                },
                onFailure = { error ->
                    showSnackbar(
                        error.toAppError(NotificationsContract.ScreenError.MarkAllNotificationsReadFailed),
                    )
                }
            )
        }
    }
}
