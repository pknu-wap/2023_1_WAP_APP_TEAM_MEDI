package com.android.mediproject.core.network.datasource.penalties

import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse

interface RecallSuspensionDataSource {

    /**
     * 의약품 회수·판매중지 목록 조회
     */
    suspend fun getRecallSuspensionList(
        pageNo: Int,
    ): RecallSuspensionListResponse

    /**
     * 의약품 회수·판매중지 정보 상세 조회
     *
     * @param company 제조사
     * @param product 제품명
     */
    suspend fun getDetailRecallSuspensionInfo(
        pageNo: Int,
        company: String?,
        product: String?,
    ): Result<DetailRecallSuspensionResponse.Body.Item.Item>
}