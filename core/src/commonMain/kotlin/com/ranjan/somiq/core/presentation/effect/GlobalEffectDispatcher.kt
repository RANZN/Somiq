package com.ranjan.somiq.core.presentation.effect

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GlobalEffectDispatcher {
    private val _effects = MutableSharedFlow<GlobalUiEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )

    val effects: SharedFlow<GlobalUiEffect> = _effects.asSharedFlow()

    fun emit(effect: GlobalUiEffect) {
        _effects.tryEmit(effect)
    }
}
