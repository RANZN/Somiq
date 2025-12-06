package com.ranjan.somiq.common.checkForUpdate

import com.ranjan.somiq.core.consts.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import kotlin.time.Duration.Companion.seconds

class CheckForUpdateRepositoryImpl(
    private val httpClient: HttpClient,
) : CheckForUpdateRepository {

    override suspend fun isUpdateNeeded(): Result<Boolean> = runCatching {
        val response = httpClient.get("$BASE_URL/checkUpdate") {
            timeout {
                requestTimeoutMillis = 5.seconds.inWholeMilliseconds
                connectTimeoutMillis = 3.seconds.inWholeMilliseconds
                socketTimeoutMillis = 5.seconds.inWholeMilliseconds
            }
        }
        return@runCatching response.body<Boolean>()
    }

}