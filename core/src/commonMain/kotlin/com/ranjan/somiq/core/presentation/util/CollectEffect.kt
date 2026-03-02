package com.ranjan.somiq.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import kotlinx.coroutines.flow.Flow

/**
 * Composable function to collect and handle UI effects from ViewModels.
 *
 * @param T The effect type that extends [com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect]
 * @param effectFlow The flow of effects to collect
 * @param onEffect The callback to handle each effect
 */
@Composable
fun <T : BaseUiEffect> CollectEffect(
    effectFlow: Flow<T>,
    onEffect: suspend (T) -> Unit
) {
    LaunchedEffect(effectFlow) {
        effectFlow.collect(onEffect)
    }
}
