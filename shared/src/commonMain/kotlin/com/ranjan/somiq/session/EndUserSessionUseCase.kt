package com.ranjan.somiq.session

import com.ranjan.somiq.auth.domain.usecase.LogoutUseCase

class EndUserSessionUseCase(
    private val logoutUseCase: LogoutUseCase,
) {
    suspend operator fun invoke() {
        logoutUseCase()
    }
}
