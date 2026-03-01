package com.ranjan.somiq.auth.ui.signup.mapper

import com.ranjan.somiq.auth.ui.signup.SignUpContract

fun SignUpContract.UiState.Error.getMessage(): String = when (this) {
    SignUpContract.UiState.Error.Name.Required -> "Name is required"
    SignUpContract.UiState.Error.Name.TooShort -> "Name is too short"

    SignUpContract.UiState.Error.Username.Required -> "Username is required"
    SignUpContract.UiState.Error.Username.TooShort -> "Username must be at least 3 characters"
    SignUpContract.UiState.Error.Username.InvalidFormat -> "Username can only contain letters, numbers, and underscores"
    SignUpContract.UiState.Error.Username.AlreadyInUse -> "Username already taken"

    SignUpContract.UiState.Error.Email.Required -> "Email is required"
    SignUpContract.UiState.Error.Email.InvalidFormat -> "Invalid email address"
    SignUpContract.UiState.Error.Email.AlreadyInUse -> "Email already in use"

    SignUpContract.UiState.Error.Password.Required -> "Password is required"
    SignUpContract.UiState.Error.Password.InvalidFormat -> "Invalid password"
    SignUpContract.UiState.Error.Password.Mismatch -> "Passwords do not match"

    SignUpContract.UiState.Error.ConfirmPassword.Required -> "Confirm password is required"
    SignUpContract.UiState.Error.ConfirmPassword.Mismatch -> "Passwords do not match"

    is SignUpContract.UiState.Error.GenericError -> errorMsg ?: "Sign up failed"
}