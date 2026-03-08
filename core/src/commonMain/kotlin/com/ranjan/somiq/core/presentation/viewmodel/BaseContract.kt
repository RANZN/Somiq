package com.ranjan.somiq.core.presentation.viewmodel

/**
 * Base contract interfaces for UI state management pattern.
 * All ViewModels should use these interfaces for their State, Intent, and Effect types.
 */
interface BaseUiState

interface BaseUiIntent

interface BaseUiEffect


data object NoState : BaseUiState
interface NoIntent : BaseUiIntent