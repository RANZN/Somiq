package com.ranjan.somiq.auth.ui.signup.mapper

import androidx.compose.runtime.Composable
import com.ranjan.somiq.auth.ui.signup.SignUpUiState
import org.jetbrains.compose.resources.stringResource
import somiq.composeapp.generated.resources.Res
import somiq.composeapp.generated.resources.*

@Composable
fun SignUpUiState.Error.getMessage() = when (this) {
    SignUpUiState.Error.Name.Required -> stringResource(Res.string.name_required)
    SignUpUiState.Error.Name.TooShort -> stringResource(Res.string.name_too_short)

    SignUpUiState.Error.Email.Required -> stringResource(Res.string.email_required)
    SignUpUiState.Error.Email.InvalidFormat -> stringResource(Res.string.invalid_email_address)
    SignUpUiState.Error.Email.AlreadyInUse -> stringResource(Res.string.email_already_in_use)

    SignUpUiState.Error.Password.Required -> stringResource(Res.string.password_required)
    SignUpUiState.Error.Password.InvalidFormat -> stringResource(Res.string.invalid_password)
    SignUpUiState.Error.Password.Mismatch -> stringResource(Res.string.passwords_do_not_match)

    SignUpUiState.Error.ConfirmPassword.Required -> stringResource(Res.string.confirm_password_required)
    SignUpUiState.Error.ConfirmPassword.Mismatch -> stringResource(Res.string.passwords_do_not_match)

    is SignUpUiState.Error.GenericError -> errorMsg ?: stringResource(Res.string.signup_failed)
}