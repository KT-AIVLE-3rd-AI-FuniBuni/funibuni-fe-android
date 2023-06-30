package com.aivle.presentation.disposal.wasteclassification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aivle.domain.model.waste.LargeCategoryResult
import com.aivle.domain.model.waste.SmallCategoryResult
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.R
import com.aivle.presentation._common.repeatOnStarted
import com.aivle.presentation._common.showToast
import com.aivle.presentation._util.BitmapUtil
import com.aivle.presentation.databinding.FragmentWasteClassificationBinding
import com.aivle.presentation.disposal.wasteclassification.WasteClassificationViewModel.Event
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import com.aivle.presentation.disposal.wasteclassification.bottomsheet.WasteCategoryBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WasteClassificationFragment"

@AndroidEntryPoint
class WasteClassificationFragment : BaseDisposalFragment<FragmentWasteClassificationBinding>(
    R.layout.fragment_waste_classification) {

    private val viewModel: WasteClassificationViewModel by viewModels()
    // private lateinit var largeCatListAdapter: LargeCategoryResultListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        // viewModel.classifyWasteImage(activityViewModel.wasteImageUri)
        viewModel.loadWasteSpecTable()
    }

    private fun initView() {
        val wasteImageBitmap = BitmapUtil.decodeFile(activityViewModel.wasteImageUri)
        binding.wasteImage.setImageBitmap(wasteImageBitmap)

        binding.btnReselect.setOnClickListener {
            showSelectCategoryBottomSheet()
        }

        binding.btnNext.setOnClickListener {

            moveNext(viewModel.allWasteSpecs.first())
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.ClassificationResult -> {
                showClassificationResult(event.document)
            }
            is Event.WasteSpecTable -> {
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showClassificationResult(document: WasteClassificationDocument) {
        binding.wasteName.text = document.first_large_category_name

        val largeResults = document.labels.map { it.large_category }
        binding.largeCategoryResultList.layoutManager = GridLayoutManager(requireContext(), largeResults.size)
        binding.largeCategoryResultList.adapter = LargeCategoryResultListAdapter().apply {
            submitList(largeResults)
        }
        val smallResults = document.labels.map { it.small_category }
    }

    private fun foo(largeResults: List<LargeCategoryResult>, smallResults: List<SmallCategoryResult>) {

    }

    private fun showSelectCategoryBottomSheet() {
        val allWasteSpecs = viewModel.allWasteSpecs
        if (allWasteSpecs.isNotEmpty()) {
            val bottomSheet = WasteCategoryBottomSheetFragment(allWasteSpecs) {
                moveNext(it)
            }
            bottomSheet.show(requireActivity().supportFragmentManager, "select-cat")
        }
    }

    private fun moveNext(wasteSpec: WasteSpec) {
        activityViewModel.selectedWasteSpec = wasteSpec
        findNavController().navigate(R.id.action_wasteClassificationFragment_to_applyChoiceFragment)
    }
}