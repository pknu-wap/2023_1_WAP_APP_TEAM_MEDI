package com.android.mediproject.core.network.datasource.news.recallsuspension

import com.android.mediproject.core.model.news.recall.DetailRecallSaleSuspensionResponse
import com.android.mediproject.core.model.news.recall.RecallSaleSuspensionListResponse

interface RecallSaleSuspensionDataSource {

    /**
     * 의약품 회수·판매중지 목록 조회
     */
    suspend fun getRecallSaleSuspensionList(
        pageNo: Int, numOfRows: Int = 15,
    ): Result<RecallSaleSuspensionListResponse>

    /**
     * 의약품 회수·판매중지 정보 상세 조회
     *
     * @param company 제조사
     * @param product 제품명
     */
    suspend fun getDetailRecallSaleSuspension(
        company: String?,
        product: String?,
    ): Result<DetailRecallSaleSuspensionResponse>
}