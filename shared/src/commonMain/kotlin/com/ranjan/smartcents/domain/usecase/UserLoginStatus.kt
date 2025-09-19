package com.ranjan.smartcents.domain.usecase

import kotlinx.coroutines.delay

class UserLoginStatus {

    suspend operator fun invoke(): Boolean {
        delay(200)
        return false
    }

}