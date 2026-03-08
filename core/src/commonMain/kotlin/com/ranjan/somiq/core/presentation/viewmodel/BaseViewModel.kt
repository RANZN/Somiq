package com.ranjan.somiq.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that provides a standardized structure for state management.
 *
 * @param S The UI State type that extends [BaseUiState]
 * @param I The UI Intent type that extends [BaseUiIntent]
 * @param E The UI Effect type that extends [BaseUiEffect]
 * @param initialState The initial state for the ViewModel
 */
abstract class BaseViewModel<S : BaseUiState, I : BaseUiIntent, E : BaseUiEffect>() : ViewModel() {

    abstract val initialState: S

    // ---- STATE ----
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected fun setState(reducer: S.() -> S) {
        _state.update { current ->
            current.reducer()
        }
    }

    // ---- EFFECT ----
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    // ---- INTENT ----
    fun handleIntent(intent: I) {
        onIntent(intent)
    }

    protected abstract fun onIntent(intent: I)
}
