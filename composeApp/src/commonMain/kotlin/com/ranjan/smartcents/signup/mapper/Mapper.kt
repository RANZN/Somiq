package com.ranjan.smartcents.signup.mapper

import androidx.compose.runtime.Composable
import com.ranjan.smartcents.presentation.signup.SignupViewModel.UiState
import org.jetbrains.compose.resources.stringResource
import smartcents.composeapp.generated.resources.Res
import smartcents.composeapp.generated.resources.*

@Composable
fun UiState.Error.getMessage() = when (this) {
    UiState.Error.NAME.Required -> stringResource(Res.string.name_required)
    UiState.Error.NAME.TooShort -> stringResource(Res.string.name_too_short)

    UiState.Error.EMAIL.Required -> stringResource(Res.string.email_required)
    UiState.Error.EMAIL.InvalidFormat -> stringResource(Res.string.invalid_email_address)
    UiState.Error.EMAIL.AlreadyInUse -> stringResource(Res.string.email_already_in_use)

    UiState.Error.PASSWORD.Required -> stringResource(Res.string.password_required)
    UiState.Error.PASSWORD.InvalidFormat -> stringResource(Res.string.invalid_password)
    UiState.Error.PASSWORD.Mismatch -> stringResource(Res.string.passwords_do_not_match)

    UiState.Error.CONFIRM_PASSWORD.Required -> stringResource(Res.string.confirm_password_required)
    UiState.Error.CONFIRM_PASSWORD.Mismatch -> stringResource(Res.string.passwords_do_not_match)

    is UiState.Error.GenericError -> errorMsg ?: stringResource(Res.string.signup_failed)
}