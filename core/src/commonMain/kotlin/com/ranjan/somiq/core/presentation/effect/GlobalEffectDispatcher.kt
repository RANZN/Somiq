package com.ranjan.somiq.core.presentation.effect

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class GlobalEffectDispatcher {
    private val _effects = Channel<GlobalUiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    suspend fun emit(effect: GlobalUiEffect) {
        _effects.send(effect)
    }
}
