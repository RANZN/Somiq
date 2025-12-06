package com.ranjan.somiq.core.data.network

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveToken(accessToken: String, refreshToken: String)
    fun clearToken()
}

class TokenProviderImpl(

) : TokenProvider {

    override fun getAccessToken(): String? {
        return null
    }

    override fun getRefreshToken(): String? {
        TODO("Not yet implemented")
    }

    override fun saveToken(accessToken: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun clearToken() {
        TODO("Not yet implemented")
    }
}