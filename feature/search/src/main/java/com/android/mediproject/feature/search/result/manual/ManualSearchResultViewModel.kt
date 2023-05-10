package com.android.mediproject.feature.search.result.manual

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.common.constant.MedicationType
import com.android.mediproject.core.domain.GetMedicineApprovalListUseCase
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject

/**
 * 수동 검색 결과 ViewModel
 *
 * @property getMedicineApprovalListUseCase 약품 목록 조회 UseCase
 * @property stateHandle 상태 저장소
 * @property _searchParameter 검색 파라미터
 * @property _searchQuery 검색어 Flow
 * @property searchResultFlow 검색 결과 Flow
 */
@HiltViewModel
class ManualSearchResultViewModel @Inject constructor(
    private val getMedicineApprovalListUseCase: GetMedicineApprovalListUseCase,
) : BaseViewModel() {

    private val _searchParameter = MutableSharedFlow<SearchParameter>(replay = 1)
    val searchParameter = _searchParameter.asSharedFlow()

    val searchResultFlow: Flow<PagingData<ApprovedMedicineItemDto>> = searchParameter.flatMapLatest { parameter ->
        getMedicineApprovalListUseCase.invoke(
            itemName = parameter.itemName,
            entpName = parameter.entpName,
            medicationType = parameter.medicationType?.description,
        ).let { pager ->
            pager.map { pagingData ->
                pagingData.map { item ->
                    item.onClick = this@ManualSearchResultViewModel::openMedicineInfo
                    item
                }
            }
        }.cachedIn(viewModelScope)
    }

    /**
     * 의약품명으로 검색
     */
    fun searchMedicinesByItemName(itemName: String) {
        viewModelScope.launch {
            val newSearchParameter = if (_searchParameter.replayCache.isNotEmpty()) {
                _searchParameter.replayCache.first().copy(itemName = itemName, entpName = null)
            } else {
                SearchParameter(itemName = itemName, entpName = null, medicationType = null)
            }
            _searchParameter.emit(newSearchParameter)
        }
    }

    /**
     * 제조사명으로 검색
     */
    fun searchMedicinesByEntpName(entpName: String) {
        viewModelScope.launch {
            val newSearchParameter = if (_searchParameter.replayCache.isNotEmpty()) {
                _searchParameter.replayCache.first().copy(itemName = null, entpName = entpName)
            } else {
                SearchParameter(itemName = null, entpName = entpName, medicationType = null)
            }
            _searchParameter.emit(newSearchParameter)
        }
    }


    /**
     * 의약품 유형으로 검색
     */
    fun searchMedicinesByMedicationType(medicationType: MedicationType?) {

        viewModelScope.launch {
            _searchParameter.emit(_searchParameter.replayCache.first().copy(medicationType = medicationType))
        }
    }

    /**
     * 약 정보 화면으로 이동
     */
    fun openMedicineInfo(approvedMedicineItemDto: ApprovedMedicineItemDto) {

    }

}

data class SearchParameter(
    val itemName: String?,
    val entpName: String?,
    val medicationType: MedicationType?,
) : Serializable