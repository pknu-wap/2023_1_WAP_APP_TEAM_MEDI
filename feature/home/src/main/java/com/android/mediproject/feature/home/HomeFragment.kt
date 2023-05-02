package com.android.mediproject.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.MediSearchbar
import com.android.mediproject.feature.comments.recentcommentlist.RecentCommentListFragment
import com.android.mediproject.feature.home.databinding.FragmentHomeBinding
import com.android.mediproject.feature.penalties.recentpenaltylist.RecentPenaltyListFragment
import com.android.mediproject.feature.search.recentsearchlist.RecentSearchListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment() : BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {

    override val fragmentViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchBar()
        initChildFragments()
    }

    /**
     * 검색바 검색 가능하도록 설정하고, 검색버튼과 AI검색 버튼이 동작하도록 초기화합니다.
     */
    private fun initSearchBar() {
        binding.searchView.setOnSearchAiBtnClickListener {
            toast("AI검색을 초기화합니다")
        }

        binding.searchView.setOnSearchBtnClickListener(object : MediSearchbar.SearchQueryCallback {
            override fun onSearchQuery(query: String) {
                toast("$query 를 검색합니다")
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToManualSearchResultNav())
            }

            override fun onEmptyQuery() {
                toast(getString(R.string.search_empty_query))
            }
        })
    }

    private fun initChildFragments() {
        // 아이템 클릭 시 처리 로직
        childFragmentManager.apply {
            setFragmentResultListener(RecentSearchListFragment.ResultKey.SEARCH_HISTORY_QUERY_KEY.name) { _, bundle ->

                bundle.apply {
                    val result = getInt(RecentSearchListFragment.ResultKey.WORD.name)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToManualSearchResultNav())
                }
            }
            setFragmentResultListener(RecentCommentListFragment.ResultKey.RESULT_KEY.name) { _, bundle ->
                bundle.apply {
                    val result = getInt(RecentCommentListFragment.ResultKey.WORD.name)
                }
            }
            setFragmentResultListener(RecentPenaltyListFragment.ResultKey.RESULT_KEY.name) { _, bundle ->
                bundle.apply {
                    val result = getInt(RecentPenaltyListFragment.ResultKey.PENALTY_ID.name)
                }
            }
        }
    }
}