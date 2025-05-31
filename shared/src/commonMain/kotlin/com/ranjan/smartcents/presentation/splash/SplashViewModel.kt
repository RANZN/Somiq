package com.ranjan.smartcents.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _finishSplash = MutableStateFlow(false)
    val finishSplash = _finishSplash.asStateFlow()

    private val _splashAction = MutableSharedFlow<SplashAction>()
    val splashAction = _splashAction.asSharedFlow()

    init {
        handleSplashTransition()
    }

    fun handleSplashTransition() {
        viewModelScope.launch {
            val splashDelay = async { delay(2000) }
            val updateCheckDeferred = async {
                delay(100)
                //Check for update
                false
            }
            val isUserLoggedInDeferred = async {
                false
            }
            val isUpdateNeeded = updateCheckDeferred.await()
            val isUserLoggedIn = isUserLoggedInDeferred.await()

            splashDelay.await()

            val action = when {
                isUserLoggedIn -> SplashAction.NavigateToHome
                else -> SplashAction.NavigateToLogin
            }

            _splashAction.emit(action)
            _finishSplash.emit(true)
        }
    }

    sealed interface SplashAction {
        object NavigateToHome : SplashAction
        object NavigateToLogin : SplashAction
    }
}