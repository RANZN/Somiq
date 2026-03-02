package com.ranjan.somiq.core.presentation.viewmodel

/**
 * Base contract interfaces for UI state management pattern.
 * All ViewModels should use these interfaces for their State, Action, and Effect types.
 */
interface BaseUiState

interface BaseUiAction

interface BaseUiEffect


data object NoState : BaseUiState
interface NoAction : BaseUiAction