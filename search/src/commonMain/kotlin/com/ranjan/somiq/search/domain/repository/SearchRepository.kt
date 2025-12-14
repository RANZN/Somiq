package com.ranjan.somiq.search.domain.repository

import com.ranjan.somiq.search.data.model.SearchResult

interface SearchRepository {
    suspend fun search(query: String): Result<SearchResult>
}
