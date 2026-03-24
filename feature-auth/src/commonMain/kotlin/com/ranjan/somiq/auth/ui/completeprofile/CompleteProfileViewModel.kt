package com.ranjan.somiq.auth.ui.completeprofile

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.usecase.CheckUserIdUseCase
import com.ranjan.somiq.auth.domain.usecase.CompleteSignupUseCase
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.Effect
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.Intent
import com.ranjan.somiq.auth.ui.completeprofile.CompleteProfileContract.UiState
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.util.isValidEmail
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompleteProfileViewModel(
    private val signupToken: String,
    private val completeSignupUseCase: CompleteSignupUseCase,
    private val checkUserIdUseCase: CheckUserIdUseCase,
    private val globalEffectDispatcher: GlobalEffectDispatcher,
) : BaseViewModel<UiState, Intent, Effect>(UiState()) {

    private var userIdDebounceJob: Job? = null

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.OnNameChange -> setState { copy(name = intent.name) }
                is Intent.OnUserIdChange -> handleUserIdChange(intent.userId)
                is Intent.OnUserIdFocusChanged -> handleUserIdFocus(intent.isFocused)
                is Intent.OnEmailChange -> setState { copy(email = intent.email) }
                Intent.AddPhotoClick -> { /* Optional: open image picker & set profilePictureUrl when wired */ }
                Intent.Submit -> handleSubmit()
            }
        }
    }

    private fun handleUserIdChange(userId: String) {
        setState { copy(userId = userId, userIdAvailable = null) }
        userIdDebounceJob?.cancel()
        val uid = userId.trim()
        if (uid.length < MIN_USERNAME_LEN_FOR_CHECK) {
            setState { copy(isCheckingUserId = false) }
            return
        }
        if (validateUserIdOnly(uid).isNotEmpty()) {
            setState { copy(userIdAvailable = null, isCheckingUserId = false) }
            return
        }
        userIdDebounceJob = viewModelScope.launch {
            delay(USERNAME_CHECK_DEBOUNCE_MS)
            runUserIdAvailabilityCheck()
        }
    }

    private fun handleUserIdFocus(isFocused: Boolean) {
        if (isFocused) return
        userIdDebounceJob?.cancel()
        viewModelScope.launch {
            runUserIdAvailabilityCheck()
        }
    }

    private suspend fun runUserIdAvailabilityCheck() {
        val uid = state.value.userId.trim()
        if (uid.length < MIN_USERNAME_LEN_FOR_CHECK) {
            setState { copy(isCheckingUserId = false) }
            return
        }
        if (validateUserIdOnly(uid).isNotEmpty()) {
            setState { copy(userIdAvailable = null, isCheckingUserId = false) }
            return
        }

        setState { copy(isCheckingUserId = true, error = emptyList()) }
        val result = checkUserIdUseCase(uid)
        result.fold(
            onSuccess = { available ->
                setState {
                    copy(
                        isCheckingUserId = false,
                        userIdAvailable = available,
                        error = if (available) emptyList() else listOf(UiState.Error.UserId.Unavailable),
                    )
                }
            },
            onFailure = {
                setState { copy(isCheckingUserId = false, userIdAvailable = null) }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar("Could not check username", duration = SnackbarDuration.Short)
                )
            }
        )
    }

    private fun validateUserIdOnly(userId: String): List<UiState.Error> {
        val errors = mutableListOf<UiState.Error>()
        if (userId.isEmpty()) errors.add(UiState.Error.UserId.Required)
        else if (userId.length < 3) errors.add(UiState.Error.UserId.TooShort)
        else if (!userId.matches(Regex("^[a-zA-Z0-9_]+$"))) errors.add(UiState.Error.UserId.InvalidFormat)
        return errors
    }

    private fun validateLocalFields(state: UiState): List<UiState.Error> {
        val errors = mutableListOf<UiState.Error>()
        if (state.name.isBlank()) errors.add(UiState.Error.Name.Required)
        else if (state.name.length < 2) errors.add(UiState.Error.Name.TooShort)

        val userIdFormatErrors = validateUserIdOnly(state.userId.trim())
        errors.addAll(userIdFormatErrors)

        if (state.email.isNotBlank() && !state.email.isValidEmail()) {
            errors.add(UiState.Error.Email.InvalidFormat)
        }

        if (userIdFormatErrors.isEmpty()) {
            when (state.userIdAvailable) {
                false -> errors.add(UiState.Error.UserId.Unavailable)
                true -> Unit
                null -> errors.add(UiState.Error.UserId.AvailabilityNotChecked)
            }
        }

        return errors
    }

    private suspend fun handleSubmit() {
        if (state.value.isCheckingUserId) {
            globalEffectDispatcher.emit(
                GlobalUiEffect.ShowSnackbar("Please wait for username check…", duration = SnackbarDuration.Short)
            )
            return
        }

        val errors = validateLocalFields(state.value)
        if (errors.isNotEmpty()) {
            setState { copy(error = errors) }
            errors.firstOrNull()?.let { e ->
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(e.getMessage(), duration = SnackbarDuration.Short)
                )
            }
            return
        }

        setState { copy(isLoading = true, error = emptyList()) }
        runCompleteSignup()
    }

    private suspend fun runCompleteSignup() {
        val s = state.value
        val result = completeSignupUseCase(
            signupToken = signupToken,
            name = s.name.trim(),
            userId = s.userId.trim(),
            email = s.email.trim().takeIf { it.isNotEmpty() },
            profilePictureUrl = s.profilePictureUrl,
        )

        when (result) {
            is AuthResult.Success -> {
                setState { copy(isLoading = false) }
                emitEffect(Effect.NavigateHome)
            }

            AuthResult.Failure.PhoneAlreadyInUse -> {
                setState { copy(isLoading = false) }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar("Session expired. Start again.", duration = SnackbarDuration.Short)
                )
            }

            AuthResult.Failure.UsernameAlreadyInUse -> {
                val err = UiState.Error.UserId.AlreadyInUse
                setState { copy(isLoading = false, error = listOf(err)) }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(err.getMessage(), duration = SnackbarDuration.Short)
                )
            }

            AuthResult.Failure.EmailAlreadyInUse -> {
                setState { copy(isLoading = false) }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar("Email already in use", duration = SnackbarDuration.Short)
                )
            }

            is AuthResult.Failure.Unknown -> {
                setState { copy(isLoading = false, error = listOf(UiState.Error.GenericError(result.message))) }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(
                        result.message ?: "Failed",
                        duration = SnackbarDuration.Short
                    )
                )
            }

            else -> setState { copy(isLoading = false) }
        }
    }

    private companion object {
        const val USERNAME_CHECK_DEBOUNCE_MS = 450L
        /** Call check-user-id when username length is at least this (after trim). */
        const val MIN_USERNAME_LEN_FOR_CHECK = 3
    }
}
