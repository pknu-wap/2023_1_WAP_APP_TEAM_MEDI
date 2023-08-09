package com.android.mediproject.feature.favoritemedicine

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicine
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.ButtonChip
import com.android.mediproject.feature.favoritemedicine.databinding.FragmentFavoriteMedicineBinding
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMedicineFragment :
    BaseFragment<FragmentFavoriteMedicineBinding, FavoriteMedicineViewModel>(
        FragmentFavoriteMedicineBinding::inflate,
    ) {

    override val fragmentViewModel: FavoriteMedicineViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        fragmentViewModel.apply {
            viewLifecycleOwner.apply {
                repeatOnStarted { token.collect { handleToken(it) } }
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                repeatOnStarted { favoriteMedicineList.collect { handleFavoriteMedicineListState(it) } }
            }
            loadTokens()
        }
    }

    private fun handleFavoriteMedicineListState(favoriteMedicineListState: UiState<List<FavoriteMedicine>>) {
        when (favoriteMedicineListState) {
            is UiState.Initial -> Unit

            is UiState.Loading -> setLoadingFavoriteMedicineVisible()

            is UiState.Success -> {
                setSuccessFavoriteMedicineVisible()
                setFavoriteMedicineList(favoriteMedicineListState.data)
            }

            is UiState.Error -> {
                log(favoriteMedicineListState.message)
            }
        }
    }

    private fun setLoadingFavoriteMedicineVisible() = binding.apply {
        favoriteMedicineList.visibility = View.GONE
        favoriteMedicineLottie.visibility = View.VISIBLE
    }

    private fun setSuccessFavoriteMedicineVisible() = binding.apply {
        favoriteMedicineList.visibility = View.VISIBLE
        favoriteMedicineLottie.visibility = View.GONE
    }

    private fun handleToken(token: TokenState<CurrentTokens>) {
        when (token) {
            is TokenState.Empty -> {}
            is TokenState.Error -> {}
            is TokenState.Tokens.RefreshExpiration -> {}
            is TokenState.Tokens.AccessExpiration -> {}
            is TokenState.Tokens.Valid -> loadFavoriteMedicines()
        }
    }

    private fun loadFavoriteMedicines() {
        fragmentViewModel.loadFavoriteMedicines()
    }

    private fun handleEvent(event: FavoriteMedicineViewModel.FavoriteMedicineEvent) {
        when (event) {
            is FavoriteMedicineViewModel.FavoriteMedicineEvent.NavigateToFavoriteMedicineMore -> navigateWithUri("medilens://main/favoriteMedicineMore_nav")
        }
    }

    private fun setFavoriteMedicineList(medicineList: List<FavoriteMedicine>) {
        clearFavoriteMedicineListView()
        if (medicineList.isNotEmpty()) showFavorteMedicine(medicineList)
        else showNoFavoriteMedicine()
    }

    private fun clearFavoriteMedicineListView() = binding.favoriteMedicineList.removeAllViews()

    private fun showFavorteMedicine(medicineList: List<FavoriteMedicine>) {
        val horizontalSpace = resources.getDimension(R.dimen.dp_4).toInt()

        medicineList.forEach { medicine ->
            binding.favoriteMedicineList.addView(
                ButtonChip<String>(
                    requireContext(),
                ).apply {
                    layoutParams = FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        setMargins(horizontalSpace, 0, horizontalSpace, 0)
                    }
                    setChipText(medicine.medicineName)
                    data = medicine.itemSeq
                    setOnChipClickListener {
                        toast(it.toString())
                    }
                },
            )
        }
    }

    private fun showNoFavoriteMedicine() {
        binding.noFavoriteMedicineTV.text = showNoFavoriteMedicineSpan()
        showNoFavoriteMedicineVisibie()
    }

    private fun showNoFavoriteMedicineSpan(): SpannableStringBuilder {
        return SpannableStringBuilder(getString(com.android.mediproject.feature.favoritemedicine.R.string.noFavoriteMedicine)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main,
                    ),
                ),
                0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(
                UnderlineSpan(),
                0,
                4,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main,
                    ),
                ),
                6, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(
                UnderlineSpan(),
                6,
                8,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
        }
    }

    private fun showNoFavoriteMedicineVisibie() = binding.apply {
        favoriteMedicineHeaderView.setMoreVisiblity(false)
        favoriteMedicineHeaderView.setExpandVisiblity(false)
        favoriteMedicineList.visibility = View.GONE
        noFavoriteMedicineTV.visibility = View.VISIBLE
    }
}
