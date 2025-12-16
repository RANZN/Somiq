package com.ranjan.somiq.splash

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import com.ranjan.somiq.common.checkForUpdate.CheckUpdateUseCase
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.presentation.viewmodel.NoAction
import com.ranjan.somiq.core.presentation.viewmodel.NoState
import com.ranjan.somiq.splash.SplashContract.Effect
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val checkUpdate: CheckUpdateUseCase,
    private val userLoginStatus: UserLoginStatus,
) : BaseViewModel<NoState, NoAction, Effect>(NoState) {

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
            isUserLoggedIn -> Effect.NavigateToHome(isUpdateNeeded)
            else -> Effect.NavigateToLogin(isUpdateNeeded)
        }

        emitEffect(action)
    }

    override fun onAction(action: NoAction) {
        // No actions needed for splash
    }

}