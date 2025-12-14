package com.ranjan.somiq.search.domain.usecase

import com.ranjan.somiq.search.data.model.SearchResult
import com.ranjan.somiq.search.domain.repository.SearchRepository

class SearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String): Result<SearchResult> = searchRepository.search(query)
}
