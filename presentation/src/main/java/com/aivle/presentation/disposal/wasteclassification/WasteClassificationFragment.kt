package com.aivle.presentation.disposal.wasteclassification

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentWasteClassificationBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import com.aivle.presentation.disposal.wasteclassification.WasteClassificationViewModel.Event
import com.aivle.presentation.disposal.wasteclassification.bottomsheet.WasteCategoryBottomSheetFragment
import com.aivle.presentation.util.common.BitmapUtil
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation.util.model.AiResult
import com.aivle.presentation.util.model.ClassificationResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasteClassificationFragment : BaseDisposalFragment<FragmentWasteClassificationBinding>(
    R.layout.fragment_waste_classification) {

    private val viewModel: WasteClassificationViewModel by viewModels()
    private lateinit var smallCatListAdapter: SmallCategoryResultListAdapter
    private lateinit var loadingDialog: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        if (activityViewModel.classificationResult == null) {
            viewModel.classifyWasteImage(activityViewModel.wasteImageLocalUri)
        }
    }

    private fun initView() {
        val wasteImageBitmap = BitmapUtil.decodeFile(activityViewModel.wasteImageLocalUri)
        binding.wasteImage.setImageBitmap(wasteImageBitmap)

        smallCatListAdapter = SmallCategoryResultListAdapter()
        binding.smallCategoryList.adapter = smallCatListAdapter

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
                    loadingDialog(true)
                }
                is Event.ImageClassification.Result -> {
                    showClassificationResult(event.result)
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
        binding.loadingView1.isVisible = isShow
        binding.loadingView2.isVisible = isShow
        binding.loadingView3.isVisible = isShow
    }

    private fun showClassificationResultEmpty() {
        binding.noDetectMessage1.isVisible = true
        binding.largeCategoryResultList.isVisible = false
        binding.smallCategoryList.isVisible = false

        showToast("물체가 탐지되지 않았습니다. 사진을 재촬영하거나 직접 분류 선택을 해주세요.", Toast.LENGTH_LONG)
    }

    private fun showClassificationResult(classificationResult: ClassificationResult) {
        activityViewModel.classificationResult = classificationResult

        val firstLargeCategoryName = classificationResult.groups.firstOrNull()?.largeCategory?.categoryName
            ?: return
        binding.wasteName.text = firstLargeCategoryName
        binding.wasteName.isVisible = true
        binding.wasteNameSubLabel.isVisible = true

        // 이미지 대분류 결과 목록
        val largeResults = classificationResult.groups.map { it.largeCategory }
            .onEachIndexed { index, result ->
                result.onClick = ::onChangedLargeCategorySelection
                result.isSelected = (index == 0)
            }

        binding.largeCategoryResultList.isVisible = true
        binding.largeCategoryResultList.layoutManager = GridLayoutManager(requireContext(), largeResults.size)
        binding.largeCategoryResultList.adapter = LargeCategoryResultListAdapter().apply {
            submitList(largeResults)
        }

        // 이미지 소분류 결과 목록
        val smallResults = classificationResult.groups.first().smallCategories
            .onEach {
                it.onClick = ::onChangedSmallCategorySelection
                it.isSelected = (it.percent != 0)
            }

        binding.smallCategoryList.isVisible = true
        smallCatListAdapter.submitList(smallResults)

        // 소분류 결과까지 나온 경우 selectedSpec 에 저장
        val smallAiResult = smallResults.find { it.percent > 0 }
            ?: return
        activityViewModel.selectedWasteSpec = smallAiResult.spec
    }

    private fun onChangedLargeCategorySelection(result: AiResult) {
        val smallCategories = activityViewModel.classificationResult!!.groups
            .find { it.largeCategory.index == result.index }!!
            .smallCategories
            .onEach {
                it.onClick = ::onChangedSmallCategorySelection
            }
        // 만약 선택된 소분류가 없는 경우 첫번째를 기본으로 선택해준다
        if (smallCategories.none { it.isSelected }) {
            smallCategories.first().isSelected = true
        }
        activityViewModel.selectedWasteSpec = smallCategories.find { it.isSelected }?.spec

        smallCatListAdapter.submitList(smallCategories)
    }

    private fun onChangedSmallCategorySelection(result: AiResult) {
        activityViewModel.selectedWasteSpec = result.spec
    }



    private fun showSelectCategoryBottomSheet() {
        val allWasteSpecs = viewModel.wasteSpecTable
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