package com.ranjan.somiq.auth.ui.signup

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.auth.domain.model.AuthResult
import com.ranjan.somiq.auth.domain.usecase.SignupUseCase
import com.ranjan.somiq.auth.ui.signup.SignUpContract.Action
import com.ranjan.somiq.auth.ui.signup.SignUpContract.Effect
import com.ranjan.somiq.auth.ui.signup.SignUpContract.UiState
import com.ranjan.somiq.auth.ui.signup.mapper.getMessage as getErrorMessage
import com.ranjan.somiq.core.presentation.effect.GlobalEffectDispatcher
import com.ranjan.somiq.core.presentation.effect.GlobalUiEffect
import com.ranjan.somiq.core.presentation.effect.SnackbarDuration
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import com.ranjan.somiq.core.util.isValidEmail
import com.ranjan.somiq.core.util.isValidPassword
import kotlinx.coroutines.launch

class SignupViewModel(
    private val signupUseCase: SignupUseCase,
    private val globalEffectDispatcher: GlobalEffectDispatcher,
) : BaseViewModel<UiState, Action, Effect>(UiState()) {

    override fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                Action.AddPictureClick -> {}

                is Action.OnNameChange -> {
                    setState { copy(name = action.name) }
                }

                is Action.OnUsernameChange -> {
                    setState { copy(username = action.username) }
                }

                is Action.OnEmailChange -> {
                    setState { copy(email = action.email) }
                }

                is Action.OnPasswordChange -> {
                    setState { copy(password = action.password) }
                }

                is Action.OnConfirmPasswordChange -> {
                    setState { copy(confirmPassword = action.confirmPassword) }
                }

                is Action.Signup -> handleLogin()

                is Action.ShowError -> {
                    val error = UiState.Error.GenericError(action.error)
                    setState {
                        copy(
                            error = listOf(error),
                            isLoading = false
                        )
                    }
                    globalEffectDispatcher.emit(
                        GlobalUiEffect.ShowSnackbar(
                            message = getErrorMessage(error),
                            duration = SnackbarDuration.Short
                        )
                    )
                }

                is Action.OnGoogleLoginClick -> {
                    // Handle Google login click
                }

                is Action.NavigateToHome -> {
                    emitEffect(Effect.NavigateToHome)
                }

            }
        }
    }

    fun validateFields(state: UiState): List<UiState.Error> {
        val errors = mutableListOf<UiState.Error>()

        // Name validation
        if (state.name.isEmpty()) {
            errors.add(UiState.Error.Name.Required)
        } else if (state.name.length < 3) {
            errors.add(UiState.Error.Name.TooShort)
        }

        // Username validation
        if (state.username.isEmpty()) {
            errors.add(UiState.Error.Username.Required)
        } else if (state.username.length < 3) {
            errors.add(UiState.Error.Username.TooShort)
        } else if (!state.username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            errors.add(UiState.Error.Username.InvalidFormat)
        }

        // Email validation
        if (state.email.isEmpty()) {
            errors.add(UiState.Error.Email.Required)
        } else if (!state.email.isValidEmail()) {
            errors.add(UiState.Error.Email.InvalidFormat)
        }

        // Password validation
        if (state.password.isEmpty()) {
            errors.add(UiState.Error.Password.Required)
        } else if (!state.password.isValidPassword()) {
            errors.add(UiState.Error.Password.InvalidFormat)
        }

        // Confirm Password validation
        if (state.confirmPassword.isEmpty()) {
            errors.add(UiState.Error.ConfirmPassword.Required)
        } else if (state.password != state.confirmPassword) {
            errors.add(UiState.Error.Password.Mismatch)
            errors.add(UiState.Error.ConfirmPassword.Mismatch)
        }

        return errors
    }

    suspend fun handleLogin() {
        val errors = validateFields(state.value)
        if (errors.isNotEmpty()) {
            setState { copy(error = errors) }
            // Show first error in snackbar
            val firstError = errors.firstOrNull()
            if (firstError != null) {
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(
                        message = getErrorMessage(firstError),
                        duration = SnackbarDuration.Short
                    )
                )
            }
            return
        }

        setState { copy(isLoading = true, error = emptyList()) }
        val result = signupUseCase(
            name = state.value.name,
            username = state.value.username,
            email = state.value.email,
            password = state.value.password
        )
        when (result) {
            AuthResult.Failure.EmailAlreadyInUse -> {
                val error = UiState.Error.Email.AlreadyInUse
                setState {
                    copy(
                        error = listOf(error),
                        isLoading = false
                    )
                }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(
                        message = getErrorMessage(error),
                        duration = SnackbarDuration.Short
                    )
                )
            }

            AuthResult.Failure.UsernameAlreadyInUse -> {
                val error = UiState.Error.Username.AlreadyInUse
                setState {
                    copy(
                        error = listOf(error),
                        isLoading = false
                    )
                }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(
                        message = getErrorMessage(error),
                        duration = SnackbarDuration.Short
                    )
                )
            }

            is AuthResult.Failure.Unknown -> {
                val error = UiState.Error.GenericError(result.message)
                setState {
                    copy(
                        error = listOf(error),
                        isLoading = false
                    )
                }
                globalEffectDispatcher.emit(
                    GlobalUiEffect.ShowSnackbar(
                        message = getErrorMessage(error),
                        duration = SnackbarDuration.Short
                    )
                )
            }

            is AuthResult.Success -> {
                handleAction(Action.NavigateToHome)
            }

            else -> {}
        }
    }

}