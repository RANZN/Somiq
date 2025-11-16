package com.ranjan.somiq.domain.usecase

import kotlinx.coroutines.delay

class UserLoginStatus {

    suspend operator fun invoke(): Boolean {
        delay(200)
        return false
    }

}