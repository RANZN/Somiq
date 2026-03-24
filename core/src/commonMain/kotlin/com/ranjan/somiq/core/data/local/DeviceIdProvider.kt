package com.ranjan.somiq.core.data.local

interface DeviceIdProvider {
    suspend fun getDeviceId(): String
}

class DeviceIdProviderImpl(
    private val tokenStorage: TokenStorage,
) : DeviceIdProvider {
    override suspend fun getDeviceId(): String = tokenStorage.getOrCreateDeviceId()
}
