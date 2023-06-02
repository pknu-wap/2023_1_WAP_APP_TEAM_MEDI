package com.android.mediproject.feature.camera.confirmdialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.feature.camera.MedicinesDetectorViewModel
import com.android.mediproject.feature.camera.R
import com.android.mediproject.feature.camera.databinding.FragmentConfirmDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class ConfirmDialogFragment : DialogFragment() {
    private var _binding: FragmentConfirmDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedicinesDetectorViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity()).apply {
            setTitle(getString(R.string.checkCountsOfMedicines))

            _binding = FragmentConfirmDialogBinding.inflate(layoutInflater, null, false)
            setView(onCreateView(layoutInflater, binding.root, savedInstanceState))
            setPositiveButton(getString(R.string.search)) { _, _ ->
                findNavController().popBackStack()
            }
            setNegativeButton(getString(R.string.close)) { _, _ ->
                findNavController().popBackStack()
            }.setCancelable(false)
        }.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun getView(): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.detectedObjects.collectLatest { objs ->
                (objs as UiState.Success).data.apply {
                    val text = "$size ${getString(R.string.checkCountsOfMedicinesMessage)}".toSpannable()
                    binding.confirmDialogTextView.text = text

                    binding.detectedObjectsRecyclerView.adapter = ImageListAdapter().apply {
                        map {
                            it.onClicked = { this@ConfirmDialogFragment.onDetectedObjectClicked() }
                            it
                        }.let { submitList(it) }
                    }
                }
            }

        }
    }

    override fun dismiss() {
        super.dismiss()
        viewModel.openCamera()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onDetectedObjectClicked() {
        findNavController().navigate(ConfirmDialogFragmentDirections.actionConfirmDialogFragmentToDetectedImageFragment())
    }

    override fun isCancelable(): Boolean = false
}