package com.ranjan.somiq.auth.ui.completeprofile

import com.ranjan.somiq.core.presentation.model.UiText
import com.ranjan.somiq.core.resources.Res
import com.ranjan.somiq.core.resources.could_not_save_profile
import com.ranjan.somiq.core.resources.invalid_email
import com.ranjan.somiq.core.resources.name_required
import com.ranjan.somiq.core.resources.name_too_short
import com.ranjan.somiq.core.resources.username_already_in_use
import com.ranjan.somiq.core.resources.username_availability_not_checked
import com.ranjan.somiq.core.resources.username_invalid_format
import com.ranjan.somiq.core.resources.username_required
import com.ranjan.somiq.core.resources.username_too_short
import com.ranjan.somiq.core.resources.username_unavailable

fun CompleteProfileContract.UiState.Error.getMessage(): UiText = when (this) {
    CompleteProfileContract.UiState.Error.Name.Required -> UiText.Resource(Res.string.name_required)
    CompleteProfileContract.UiState.Error.Name.TooShort -> UiText.Resource(Res.string.name_too_short)

    CompleteProfileContract.UiState.Error.UserId.Required -> UiText.Resource(Res.string.username_required)
    CompleteProfileContract.UiState.Error.UserId.TooShort -> UiText.Resource(Res.string.username_too_short)
    CompleteProfileContract.UiState.Error.UserId.InvalidFormat ->
        UiText.Resource(Res.string.username_invalid_format)
    CompleteProfileContract.UiState.Error.UserId.AlreadyInUse ->
        UiText.Resource(Res.string.username_already_in_use)
    CompleteProfileContract.UiState.Error.UserId.AvailabilityNotChecked ->
        UiText.Resource(Res.string.username_availability_not_checked)
    CompleteProfileContract.UiState.Error.UserId.Unavailable ->
        UiText.Resource(Res.string.username_unavailable)

    CompleteProfileContract.UiState.Error.Email.InvalidFormat -> UiText.Resource(Res.string.invalid_email)

    is CompleteProfileContract.UiState.Error.GenericError ->
        errorMsg?.let { UiText.Dynamic(it) } ?: UiText.Resource(Res.string.could_not_save_profile)
}
