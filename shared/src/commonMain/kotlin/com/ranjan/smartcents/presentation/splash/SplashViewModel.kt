package com.ranjan.smartcents.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.smartcents.domain.usecase.CheckUpdate
import com.ranjan.smartcents.domain.usecase.UserLoginStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val checkUpdate: CheckUpdate,
    private val userLoginStatus: UserLoginStatus,
) : ViewModel() {

    private val _splashAction = MutableSharedFlow<SplashAction>(replay = 1)
    val splashAction = _splashAction.asSharedFlow()

    init {
        handleSplashTransition()
    }

    fun handleSplashTransition() = viewModelScope.launch {
        val splashDelay = async { delay(3.seconds) }
        val updateCheckDeferred = async { checkUpdate() }
        val isUserLoggedInDeferred = async { userLoginStatus() }

        splashDelay.await()

        val isUpdateNeeded = if (updateCheckDeferred.isCompleted) {
            updateCheckDeferred.await()
        } else {
            updateCheckDeferred.cancel()
            false
        }

        val isUserLoggedIn = isUserLoggedInDeferred.await()

        val action = when {
            isUserLoggedIn -> SplashAction.NavigateToHome(isUpdateNeeded)
            else -> SplashAction.NavigateToLogin(isUpdateNeeded)
        }

        _splashAction.emit(action)
    }

    sealed interface SplashAction {
        data class NavigateToHome(val isUpdateNeeded: Boolean) : SplashAction
        data class NavigateToLogin(val isUpdateNeeded: Boolean) : SplashAction
    }
}