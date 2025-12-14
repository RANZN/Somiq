package com.ranjan.somiq.search.data.repository

import com.ranjan.somiq.core.consts.BASE_URL
import com.ranjan.somiq.core.data.network.NetworkException
import com.ranjan.somiq.search.data.model.SearchResult
import com.ranjan.somiq.search.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class SearchRepositoryImpl(
    private val httpClient: HttpClient
) : SearchRepository {

    override suspend fun search(query: String): Result<SearchResult> {
        return try {
            val response = httpClient.get("$BASE_URL/v1/search?q=$query")
            if (response.status == HttpStatusCode.OK) {
                try {
                    val searchResult = response.body<SearchResult>()
                    Result.success(searchResult)
                } catch (e: kotlinx.serialization.SerializationException) {
                    Result.failure(Exception("Failed to parse search results: ${e.message}"))
                }
            } else {
                Result.failure(Exception("Failed to search: ${response.status}"))
            }
        } catch (e: NetworkException.NoNetwork) {
            Result.failure(e)
        } catch (e: NetworkException.Timeout) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to search: ${e.message}"))
        }
    }
}
