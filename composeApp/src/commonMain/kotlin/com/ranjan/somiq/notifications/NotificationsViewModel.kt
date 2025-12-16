package com.ranjan.somiq.notifications

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.home.domain.repository.NotificationRepository
import com.ranjan.somiq.home.domain.usecase.GetNotificationsUseCase
import com.ranjan.somiq.notifications.NotificationsContract.Action
import com.ranjan.somiq.notifications.NotificationsContract.Effect
import com.ranjan.somiq.notifications.NotificationsContract.UiState
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationsViewModel : BaseViewModel<UiState, Action, Effect>(UiState()), KoinComponent {
    private val getNotificationsUseCase: GetNotificationsUseCase by inject()
    private val notificationRepository: NotificationRepository by inject()

    init {
        handleAction(Action.LoadNotifications)
        handleAction(Action.LoadUnreadCount)
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.LoadNotifications -> loadNotifications()
            is Action.LoadUnreadCount -> loadUnreadCount()
            is Action.MarkAsRead -> markAsRead(action.notificationId)
            is Action.MarkAllAsRead -> markAllAsRead()
            is Action.Refresh -> {
                loadNotifications()
                loadUnreadCount()
            }
        }
    }

    private fun loadNotifications() {
        setState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getNotificationsUseCase().fold(
                onSuccess = { notifications ->
                    setState {
                        copy(
                            notifications = notifications,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    setState {
                        copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load notifications"
                        )
                    }
                }
            )
        }
    }

    private fun loadUnreadCount() {
        viewModelScope.launch {
            notificationRepository.getUnreadCount().fold(
                onSuccess = { count ->
                    setState { copy(unreadCount = count) }
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
                    emitEffect(Effect.ShowError(
                        error.message ?: "Failed to mark as read"
                    ))
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
                    emitEffect(Effect.ShowError(
                        error.message ?: "Failed to mark all as read"
                    ))
                }
            )
        }
    }
}

