package com.ranjan.somiq.signup.mapper

import androidx.compose.runtime.Composable
import com.ranjan.somiq.presentation.signup.SignupViewModel.UiState
import org.jetbrains.compose.resources.stringResource
import somiq.composeapp.generated.resources.Res
import somiq.composeapp.generated.resources.*

@Composable
fun UiState.Error.getMessage() = when (this) {
    UiState.Error.Name.Required -> stringResource(Res.string.name_required)
    UiState.Error.Name.TooShort -> stringResource(Res.string.name_too_short)

    UiState.Error.Email.Required -> stringResource(Res.string.email_required)
    UiState.Error.Email.InvalidFormat -> stringResource(Res.string.invalid_email_address)
    UiState.Error.Email.AlreadyInUse -> stringResource(Res.string.email_already_in_use)

    UiState.Error.Password.Required -> stringResource(Res.string.password_required)
    UiState.Error.Password.InvalidFormat -> stringResource(Res.string.invalid_password)
    UiState.Error.Password.Mismatch -> stringResource(Res.string.passwords_do_not_match)

    UiState.Error.ConfirmPassword.Required -> stringResource(Res.string.confirm_password_required)
    UiState.Error.ConfirmPassword.Mismatch -> stringResource(Res.string.passwords_do_not_match)

    is UiState.Error.GenericError -> errorMsg ?: stringResource(Res.string.signup_failed)
}