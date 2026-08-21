package com.ranjan.somiq.app.search.data.repository

import com.ranjan.somiq.core.data.network.safeApiCall
import com.ranjan.somiq.app.search.data.model.SearchResultResponse
import com.ranjan.somiq.app.search.domain.model.SearchResult
import com.ranjan.somiq.app.search.data.mapper.toDomain
import com.ranjan.somiq.app.search.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SearchRepositoryImpl(
    private val httpClient: HttpClient
) : SearchRepository {

    override suspend fun search(query: String): Result<SearchResult> {
        return safeApiCall(
            apiCall = { httpClient.get("v1/search?q=$query") },
            onSuccess = { response ->
                response.body<SearchResultResponse>().toDomain()
            }
        )
    }
}
