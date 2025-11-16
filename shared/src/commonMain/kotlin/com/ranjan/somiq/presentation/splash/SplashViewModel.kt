package com.ranjan.somiq.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.domain.usecase.CheckUpdate
import com.ranjan.somiq.domain.usecase.UserLoginStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val checkUpdate: CheckUpdate,
    private val userLoginStatus: UserLoginStatus,
) : ViewModel() {

    private val _splashAction = Channel<SplashAction>(Channel.BUFFERED)
    val splashAction = _splashAction.receiveAsFlow()

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

        _splashAction.send(action)
    }

    sealed interface SplashAction {
        data class NavigateToHome(val isUpdateNeeded: Boolean) : SplashAction
        data class NavigateToLogin(val isUpdateNeeded: Boolean) : SplashAction
    }
}