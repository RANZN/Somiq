package com.ranjan.somiq.auth.ui.signup.mapper

import androidx.compose.runtime.Composable
import com.ranjan.somiq.auth.ui.signup.SignUpUiState

@Composable
fun SignUpUiState.Error.getMessage() = when (this) {
    SignUpUiState.Error.Name.Required -> "Name is required"
    SignUpUiState.Error.Name.TooShort -> "Name is too short"

    SignUpUiState.Error.Email.Required -> "Email is required"
    SignUpUiState.Error.Email.InvalidFormat -> "Invalid email address"
    SignUpUiState.Error.Email.AlreadyInUse -> "Email already in use"

    SignUpUiState.Error.Password.Required -> "Password is required"
    SignUpUiState.Error.Password.InvalidFormat -> "Invalid password"
    SignUpUiState.Error.Password.Mismatch -> "Passwords do not match"

    SignUpUiState.Error.ConfirmPassword.Required -> "Confirm password is required"
    SignUpUiState.Error.ConfirmPassword.Mismatch -> "Passwords do not match"

    is SignUpUiState.Error.GenericError -> errorMsg ?: "Sign up failed"
}