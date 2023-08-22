package com.android.mediproject.core.domain

import com.android.mediproject.core.data.search.SearchHistoryRepository
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto
import com.android.mediproject.core.database.searchhistory.toSearchHistoryItemDto
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun insertSearchHistory(query: String) =
        searchHistoryRepository.insertSearchHistory(SearchHistoryDto(query))

    fun getSearchHistoryList(count: Int) =
        searchHistoryRepository.getSearchHistoryList(count).mapLatest {
            it.map { dto ->
                dto.toSearchHistoryItemDto()
            }
        }

    suspend fun deleteSearchHistory(id: Long) = searchHistoryRepository.deleteSearchHistory(id)

    suspend fun deleteAllSearchHistory() = searchHistoryRepository.deleteAllSearchHistory()
}