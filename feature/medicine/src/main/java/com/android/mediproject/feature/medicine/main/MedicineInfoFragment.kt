package com.android.mediproject.feature.medicine.main

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.uiutil.SystemBarController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.R
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted
import javax.inject.Inject

/**
 * 약 정보 화면
 *
 */
@AndroidEntryPoint
class MedicineInfoFragment : BaseFragment<FragmentMedicineInfoBinding, MedicineInfoViewModel>(FragmentMedicineInfoBinding::inflate) {

    override val fragmentViewModel: MedicineInfoViewModel by viewModels()

    private val navArgs by navArgs<MedicineInfoArgs>()

    @Inject lateinit var systemBarStyler: SystemBarController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.setMedicinePrimaryInfo(navArgs)
        setBarStyle()

        binding.apply {
            systemBarStyler.changeMode(topViews = listOf(SystemBarStyler.ChangeView(topAppBar, SystemBarStyler.SpacingType.PADDING)))
            viewModel = fragmentViewModel
            medicineInfoArgs = navArgs

            root.doOnPreDraw {
                topAppBar.removeOnOffsetChangedListener(null)
                // smoothly hide medicinePrimaryInfoViewgroup when collapsing toolbar
                topAppBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                    medicineInfoBar.alpha = 1.0f + (verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat())
                    medicineInfoBar2.alpha = -(verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat())

                    // 스크롤 할 때 마다 medicinePrimaryInfoViewgroup의 투명도 조정
                    medicinePrimaryInfoViewgroup.alpha = 1.0f + (verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat()).apply {
                        if (this == -1.0f) {
                            medicinePrimaryInfoViewgroup.visibility = View.INVISIBLE
                        } else if (this > -0.8f) {
                            medicinePrimaryInfoViewgroup.isVisible = true
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            launch {
                fragmentViewModel.medicineDetails.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            initTabs()
                            LoadingDialog.dismiss()
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismiss()
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showLoadingDialog(requireContext(), null)
                        }

                        is UiState.Initial -> {}

                    }
                }
            }

            launch {
                fragmentViewModel.eventState.collectLatest {
                    when (it) {
                        is EventState.Favorite -> {
                            binding.interestBtn.isEnabled = it.lockChecked
                            binding.interestBtn.isChecked = it.isFavorite
                        }

                        is EventState.ScrollToBottom -> {
                            binding.topAppBar.setExpanded(false, true)
                        }
                    }
                }

            }
        }

    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    medicineInfoBar,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )

        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    medicineInfoBar2,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )
    }


    // 탭 레이아웃 초기화
    private fun initTabs() {

        binding.apply {
            contentViewPager.adapter = MedicineInfoPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            // 탭 레이아웃에 탭 추가
            resources.getStringArray(R.array.medicineInfoTab).also { tabTextList ->
                TabLayoutMediator(tabLayout, contentViewPager) { tab, position ->
                    tab.text = tabTextList[position]
                }.attach()
            }
        }

    }


}
