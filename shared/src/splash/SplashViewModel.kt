package com.ranjan.somiq.splash

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.usecase.UserLoginStatus
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.presentation.viewmodel.NoIntent
import com.ranjan.somiq.splash.SplashContract.Effect
import com.ranjan.somiq.splash.data.CheckUpdateUseCase
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkUpdate: CheckUpdateUseCase,
    private val userLoginStatus: UserLoginStatus,
) : BaseViewModel<NoIntent, Effect>() {
    init {
        handleSplashTransition()
    }
    fun handleSplashTransition() = viewModelScope.launch {
        val updateCheckDeferred = async { checkUpdate.invoke() }
        val isUserLoggedInDeferred = async { userLoginStatus() }
        delay(3.seconds)
        val isUserLoggedIn = isUserLoggedInDeferred.await()
        val isUpdateNeeded = if (updateCheckDeferred.isCompleted) {
            updateCheckDeferred.await()
        } else {
            updateCheckDeferred.cancel()
            false
        }
        val action = when {
            isUserLoggedIn -> Effect.NavigateToHome(isUpdateNeeded)
            else -> Effect.NavigateToLogin(isUpdateNeeded)
        }
        emitEffect(action)
    }
    override fun onIntent(intent: NoIntent) {
        // No intents needed for splash
    }
}
