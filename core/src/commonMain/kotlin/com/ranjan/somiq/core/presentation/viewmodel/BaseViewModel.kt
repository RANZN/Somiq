package com.ranjan.somiq.core.presentation.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.core.presentation.error.AppError
import com.ranjan.somiq.core.presentation.error.BaseScreenError
import com.ranjan.somiq.core.presentation.error.toUiText
import com.ranjan.somiq.core.presentation.model.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that provides a standardized structure for handling UI Intents and UI Effects.
 * ViewModels extending this class manage state locally using standard [kotlinx.coroutines.flow.MutableStateFlow].
 *
 * @param I The UI Intent type that extends [BaseUiIntent]
 * @param E The UI Effect type that extends [BaseUiEffect]
 */
abstract class BaseViewModel<I : BaseUiIntent, E : BaseUiEffect> : ViewModel() {
    // ---- EFFECT ----
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    private val _commonEffect = Channel<BaseUiEffect.Common>(Channel.BUFFERED)
    val commonEffect: Flow<BaseUiEffect.Common> = _commonEffect.receiveAsFlow()

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    protected fun showSnackbar(
        message: UiText,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration? = null,
    ) {
        viewModelScope.launch {
            _commonEffect.send(
                BaseUiEffect.Common.ShowSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    withDismissAction = withDismissAction,
                    duration = duration,
                ),
            )
        }
    }

    protected fun showSnackbar(error: AppError) {
        showSnackbar(error.toUiText())
    }

    protected fun showSnackbar(error: BaseScreenError) {
        showSnackbar(error.toUiText())
    }

    // ---- INTENT ----
    fun handleIntent(intent: I) {
        onIntent(intent)
    }

    protected abstract fun onIntent(intent: I)
}
