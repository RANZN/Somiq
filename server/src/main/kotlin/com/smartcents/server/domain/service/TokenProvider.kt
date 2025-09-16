package com.smartcents.server.domain.service

import com.smartcents.server.domain.model.User

interface TokenProvider {
    fun createToken(user: User) : String
}