package com.ranjan.somiq.app.search.domain.repository

import com.ranjan.somiq.app.search.data.model.SearchResult

interface SearchRepository {
    suspend fun search(query: String): Result<SearchResult>
}
