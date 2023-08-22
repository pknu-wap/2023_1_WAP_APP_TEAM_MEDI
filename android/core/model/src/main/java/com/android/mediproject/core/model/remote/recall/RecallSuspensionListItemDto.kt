package com.android.mediproject.core.model.remote.recall

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * 리콜 폐기 목록 별 데이터 클래스
 *
 * @property enfrcYn 강제여부
 * @property entrps 업체명
 * @property itemSeq 일련번호
 * @property product 제품명
 * @property recallCommandDate 리콜 명령 일자
 * @property destructionOrderDate 폐기 명령 일자
 * @property reason 폐기 사유
 *
 */
data class RecallSuspensionListItemDto(
    val enfrcYn: String?, // N
    val entrps: String, // 동국제약(주)
    val itemSeq: String, // 201208461
    val product: String, // 니자틴캡슐(니자티딘)
    val recallCommandDate: LocalDate?, // 20230504
    val destructionOrderDate: LocalDate?, // 20230503000000
    val reason: String, // 안정성 시험결과 NDMA 기준 초과에 따른 사전예방적 조치로 시중 유통품에 대해 영업자회수
    var onClick: ((RecallSuspensionListItemDto) -> Unit)? = null
)

fun RecallSuspensionListResponse.Body.Item.Item.toDto(): RecallSuspensionListItemDto {
    return RecallSuspensionListItemDto(
        enfrcYn = enfrcYn,
        entrps = entrps ?: "",
        itemSeq = itemSeq ?: "",
        product = product ?: "",
        recallCommandDate = recallCommandDate?.let {
            java.time.LocalDate.parse(it.toString(), dateFormatter).toKotlinLocalDate()
        },
        destructionOrderDate = rtrlCommandDt?.let {
            LocalDateTime.parse(it.toString(), dateTimeFormatter).toKotlinLocalDateTime().date
        },
        reason = rtrvlResn ?: ""
    )
}

private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")