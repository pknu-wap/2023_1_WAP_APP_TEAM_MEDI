package com.android.mediproject.feature.news.recallsalesuspension

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.domain.GetRecallSaleSuspensionUseCase
import com.android.mediproject.core.model.news.recall.DetailRecallSuspension
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailRecallSuspensionViewModel @Inject constructor(
    private val getRecallSaleSuspensionUseCase: GetRecallSaleSuspensionUseCase,
    private val savedStateHandle: SavedStateHandle,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _detailRecallSuspension = MutableStateFlow<UiState<DetailRecallSuspension>>(UiState.Loading)
    val detailRecallSuspension get() = _detailRecallSuspension.asStateFlow()

    /**
     * 회수 폐기 공고 목록을 로드
     */
    init {
        viewModelScope.launch(ioDispatcher) {
            val productName: String = checkNotNull(savedStateHandle["product"])
            getRecallSaleSuspensionUseCase.getDetailRecallSaleSuspension(product = productName, company = null).collectLatest {
                _detailRecallSuspension.value = it.fold(
                    onSuccess = { item ->
                        UiState.Success(item)
                    },
                    onFailure = { throwable ->
                        UiState.Error(throwable.message ?: "Failed to get detail recall suspension info")
                    },
                )
            }
        }
    }


}
