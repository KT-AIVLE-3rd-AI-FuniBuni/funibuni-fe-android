package com.aivle.presentation.disposal.wasteclassification

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.R
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation.util.common.BitmapUtil
import com.aivle.presentation.databinding.FragmentWasteClassificationBinding
import com.aivle.presentation.disposal.wasteclassification.WasteClassificationViewModel.Event
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import com.aivle.presentation.disposal.wasteclassification.bottomsheet.WasteCategoryBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

private const val TAG = "WasteClassificationFragment"

@AndroidEntryPoint
class WasteClassificationFragment : BaseDisposalFragment<FragmentWasteClassificationBinding>(
    R.layout.fragment_waste_classification) {

    private val viewModel: WasteClassificationViewModel by viewModels()
    private lateinit var loadingDialog: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        viewModel.classifyWasteImage(activityViewModel.wasteImageLocalUri)
        //viewModel.loadWasteSpecTable()
    }

    private fun initView() {
        val wasteImageBitmap = BitmapUtil.decodeFile(activityViewModel.wasteImageLocalUri)
        binding.wasteImage.setImageBitmap(wasteImageBitmap)

        binding.btnReselect.setOnClickListener {
            showSelectCategoryBottomSheet()
        }

        binding.btnNext.setOnClickListener {
            activityViewModel.selectedWasteSpec?.let {
                moveNext(it)
            }
        }

        loadingDialog = ProgressDialog(requireContext()).also {
            it.setContentView(R.layout.dialog_progress_ai)
            it.setCancelable(false)
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event ->
            if (event is Event.ImageClassification.Loading) {
                loadingDialog(true)
            } else {
                loadingDialog(false)
            }

            when (event) {
                is Event.None -> {
                }
                is Event.ImageClassification.Loading -> {
                }
                is Event.ImageClassification.Result -> {
                    showClassificationResult(event.document)
                }
                is Event.ImageClassification.Empty -> {
                    showClassificationResultEmpty()
                }
                is Event.WasteSpecTable -> {
                }
                is Event.Failure -> {
                    showToast(event.message)
                }
            }
        }
    }

    private fun loadingDialog(isShow: Boolean) {
        if (isShow) {
            loadingDialog.show()
        } else {
            loadingDialog.hide()
        }
    }

    private fun showClassificationResult(document: WasteClassificationDocument) {
        activityViewModel.classificationResult = document

        binding.wasteName.text = document.first_large_category_name
            ?: return
        binding.wasteNameSubLabel.isVisible = true

        // 이미지 대분류 결과 목록
        val largeResults = document.labels.map { it.large_category }
        binding.largeCategoryResultList.layoutManager = GridLayoutManager(requireContext(), largeResults.size)
        binding.largeCategoryResultList.adapter = LargeCategoryResultListAdapter().apply {
            submitList(largeResults)
        }

        // 이미지 소분류 결과
        val smallResult = document.labels.firstOrNull { it.small_category != null }
            ?.small_category
        if (smallResult == null) {
            val firstLargeCategoryIndex = largeResults.first().index_large_category
            val wasteSpec = document.all_waste_specs.find { it.index_large_category == firstLargeCategoryIndex }
            activityViewModel.selectedWasteSpec = wasteSpec
        } else {
            binding.smallCategoryName.text = smallResult.small_category_name
            binding.smallCategoryProbability.text = "${(smallResult.probability * 100).roundToInt()}%"

            val firstSmallCategoryIndex = smallResult.index_small_category
            val wasteSpec = document.all_waste_specs.find { it.index_small_category == firstSmallCategoryIndex }
            activityViewModel.selectedWasteSpec = wasteSpec
        }
    }

    private fun showClassificationResultEmpty() {
        showToast("사진에서 이미지를 검출하지 못했습니다ㅜㅜ")
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