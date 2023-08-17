package com.android.mediproject.feature.news.recallsalesuspension

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailRecallSaleSuspensionViewModel @Inject constructor(
    private val getRecallSaleSuspensionUseCase: GetRecallSaleSuspensionUseCase,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _detail = MutableStateFlow<UiState<DetailRecallSuspension>>(UiState.Loading)
    val detail get() = _detail.asStateFlow()

    fun load(productName: String) {
        viewModelScope.launch(ioDispatcher) {
            _detail.value = UiState.Loading
            getRecallSaleSuspensionUseCase.getDetailRecallSaleSuspension(product = productName, company = null).run {
                _detail.value = fold(
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

    fun clear() {
        viewModelScope.launch {
            _detail.value = UiState.Loading
        }
    }
}