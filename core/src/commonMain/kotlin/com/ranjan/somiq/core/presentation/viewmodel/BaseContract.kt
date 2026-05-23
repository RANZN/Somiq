package com.ranjan.somiq.core.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Base contract interfaces for UI state management pattern.
 * All ViewModels should use these interfaces for their State, Intent, and Effect types.
 */

@Stable
interface BaseUiState

@Immutable
interface BaseUiIntent

interface BaseUiEffect


data object NoState : BaseUiState
interface NoIntent : BaseUiIntent