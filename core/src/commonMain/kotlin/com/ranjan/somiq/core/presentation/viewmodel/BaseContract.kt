package com.ranjan.somiq.core.presentation.viewmodel

import com.ranjan.somiq.core.presentation.model.UiText

/**
 * Base contract interfaces for UI state management pattern.
 * All ViewModels should use these interfaces for their State, Intent, and Effect types.
 */

interface BaseScreenError {
    fun toUiText(): UiText?
}

interface BaseUiState

interface BaseUiIntent

interface BaseUiEffect


data object NoState : BaseUiState
interface NoIntent : BaseUiIntent