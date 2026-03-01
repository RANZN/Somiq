package com.ranjan.somiq.app.search.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.app.search.data.model.SearchResult
import com.ranjan.somiq.app.search.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SearchRepositoryImpl(
    private val httpClient: HttpClient
) : SearchRepository {

    override suspend fun search(query: String): Result<SearchResult> {
        return safeApiCall(
            apiCall = { httpClient.get("$BASE_URL/v1/search?q=$query") },
            errorMessage = "Failed to search"
        )
    }
}
