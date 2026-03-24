package com.ranjan.somiq.auth.ui.completeprofile

fun CompleteProfileContract.UiState.Error.getMessage(): String = when (this) {
    CompleteProfileContract.UiState.Error.Name.Required -> "Name is required"
    CompleteProfileContract.UiState.Error.Name.TooShort -> "Name is too short"

    CompleteProfileContract.UiState.Error.UserId.Required -> "Username is required"
    CompleteProfileContract.UiState.Error.UserId.TooShort -> "Username must be at least 3 characters"
    CompleteProfileContract.UiState.Error.UserId.InvalidFormat ->
        "Username can only contain letters, numbers, and underscores"
    CompleteProfileContract.UiState.Error.UserId.AlreadyInUse -> "This username is already taken"
    CompleteProfileContract.UiState.Error.UserId.AvailabilityNotChecked ->
        "Wait for the username check to finish, or finish typing and move to another field"
    CompleteProfileContract.UiState.Error.UserId.Unavailable -> "This username is not available"

    CompleteProfileContract.UiState.Error.Email.InvalidFormat -> "Invalid email address"

    is CompleteProfileContract.UiState.Error.GenericError -> errorMsg ?: "Could not save profile"
}
