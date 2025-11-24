package com.ranjan.somiq.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import com.ranjan.somiq.common.checkForUpdate.CheckUpdateUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val checkUpdate: CheckUpdateUseCase,
    private val userLoginStatus: UserLoginStatus,
) : ViewModel() {

    private val _splashEvents = Channel<SplashEvents>(Channel.BUFFERED)
    val splashAction = _splashEvents.receiveAsFlow()

    init {
        handleSplashTransition()
    }

    fun handleSplashTransition() = viewModelScope.launch {
        val splashDelay = async { delay(3.seconds) }
        val updateCheckDeferred = async { checkUpdate.invoke() }
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
            isUserLoggedIn -> SplashEvents.NavigateToHome(isUpdateNeeded)
            else -> SplashEvents.NavigateToLogin(isUpdateNeeded)
        }

        _splashEvents.send(action)
    }

}