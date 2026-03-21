package com.ranjan.somiq.app.search.domain.usecase

import com.ranjan.somiq.app.search.data.model.SearchResult
import com.ranjan.somiq.app.search.domain.repository.SearchRepository

class SearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String): Result<SearchResult> = searchRepository.search(query)
}
