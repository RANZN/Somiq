package com.ranjan.somiq.auth.ui.login.mapper

import com.ranjan.somiq.auth.ui.login.LoginContract

fun LoginContract.UiState.Errors.getErrorMessage(): String = when (this) {
    LoginContract.UiState.Errors.INVALID_EMAIL -> "Invalid email address"
    LoginContract.UiState.Errors.INVALID_PASSWORD -> "Invalid password"
    LoginContract.UiState.Errors.INVALID_CREDENTIALS -> "Invalid email and password"
    LoginContract.UiState.Errors.LOGIN_FAILED -> "Login failed. Please try again."
}
