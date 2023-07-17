package com.android.mediproject.feature.home

import MutableEventFlow
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher, private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<HomeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: HomeEvent) = viewModelScope.launch { _eventFlow.emit(event) }

    fun navigateToSearch() = event(HomeEvent.NavigateToSearch)

    sealed class HomeEvent {
        object NavigateToSearch : HomeEvent()
    }

    val headerText = savedStateHandle.getStateFlow<Spanned>("headerText", SpannedString.valueOf(""))

    fun initHeaderText(text: String) = viewModelScope.launch(defaultDispatcher) {

        SpannableStringBuilder(text).apply {

            val underline1Idx = text.indexOf("찾고") to text.indexOf("찾고") + 2

            setSpan(UnderlineSpan(), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(RelativeSizeSpan(1.2f), underline1Idx.first, underline1Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            val underline2Idx = text.indexOf("소통") to text.indexOf("소통") + 2

            setSpan(UnderlineSpan(), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(RelativeSizeSpan(1.2f), underline2Idx.first, underline2Idx.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            savedStateHandle["headerText"] = this
        }
    }
}
