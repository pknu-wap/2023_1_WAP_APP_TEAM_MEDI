package com.android.mediproject.core.model.medicine.medicineapproval

/**
 * 의약품 검색 결과 중 하나의 아이템 정보를 담는 데이터 클래스
 *
 * @property itemSeq 일련번호
 * @property itemName 한글 의약품 명
 * @property itemEngName 영문 의약품 명
 * @property entpName 업체명
 * @property entpEngName 업체 영문명
 * @property entpSeq 업체 일련번호
 * @property entpNo 업체 번호
 * @property itemPermitDate 아이템 허가 일자
 * @property induty 업종
 * @property prdlstStdrCode 제품표준코드
 * @property medicineType 특수약품 구분
 * @property prductType 제품유형
 * @property prductPrmisnNo 제품허가번호
 * @property itemIngrName 성분명
 * @property itemIngrCnt 성분수
 * @property imgUrl 대표 제품 이미지 URL
 * @property permitKindCode 허가종류코드
 * @property cancelDate 취소 일자
 * @property cancelName 취소 여부
 * @property ediCode EDI코드
 * @property bizrno 사업자등록번호
 */

data class ApprovedMedicineItemDto(
    val itemSeq: Long,
    val itemName: String,
    val itemEngName: String?,
    val entpName: String,
    val entpEngName: String?,
    val entpSeq: String,
    val entpNo: String,
    val itemPermitDate: String? = null,
    val induty: String? = null,
    val prdlstStdrCode: String? = null,
    val medicineType: String,
    val prductType: String,
    val prductPrmisnNo: String? = null,
    val itemIngrName: String,
    val itemIngrCnt: String,
    val imgUrl: String?,
    val permitKindCode: String? = null,
    val cancelDate: String? = null,
    val cancelName: String? = null,
    val ediCode: String? = null,
    val bizrno: String? = null,
    var onClick: ((ApprovedMedicineItemDto) -> Unit)? = null,
)

fun Item.toDto() = ApprovedMedicineItemDto(itemSeq = itemSeq.toLong(),
    itemName = itemName,
    itemEngName = itemEngName,
    entpName = entpName,
    entpEngName = entpEngName,
    entpSeq = entpSeq,
    entpNo = entpNo,
    itemPermitDate = itemPermitDate,
    induty = induty,
    prdlstStdrCode = prdlstStdrCode,
    medicineType = medicineType,
    prductType = prductType,
    prductPrmisnNo = prductPrmisnNo,
    itemIngrName = itemIngrName,
    itemIngrCnt = itemIngrCnt,
    imgUrl = bigPrdtImgUrl,
    permitKindCode = permitKindCode,
    cancelDate = cancelDate,
    cancelName = cancelName,
    ediCode = ediCode,
    bizrno = bizrno)