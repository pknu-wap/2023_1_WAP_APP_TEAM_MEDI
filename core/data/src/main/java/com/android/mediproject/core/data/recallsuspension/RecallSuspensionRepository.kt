package com.android.mediproject.core.data.recallsuspension

import androidx.paging.PagingData
import com.android.mediproject.core.model.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.recall.RecallSuspensionListResponse
import kotlinx.coroutines.flow.Flow

interface RecallSuspensionRepository {
    fun getRecallDisposalList(
    ): Flow<PagingData<RecallSuspensionListResponse.Item.Item>>

    suspend fun getRecentRecallDisposalList(
        pageNo: Int = 1, numOfRows: Int = 15,
    ): Result<List<RecallSuspensionListResponse.Item.Item>>

    fun getDetailRecallSuspension(
        company: String?, product: String?,
    ): Flow<Result<DetailRecallSuspensionResponse.Item.Item>>
}