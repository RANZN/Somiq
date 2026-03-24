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
 * @param initialState First snapshot for [state]. Pass `UiState(...)` into `super(...)` using
 *   constructor parameters (e.g. `phone`) so values are available before [ViewModel] init; avoid
 *   overriding a property that reads subclass `val` fields, which are not initialized when [BaseViewModel] runs.
 */
abstract class BaseViewModel<S : BaseUiState, I : BaseUiIntent, E : BaseUiEffect>(
    val initialState: S
) : ViewModel() {

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
