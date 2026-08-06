package com.ranjan.somiq.app.search.domain.repository

import com.ranjan.somiq.app.search.domain.model.SearchResult

interface SearchRepository {
    suspend fun search(query: String): Result<SearchResult>
}
