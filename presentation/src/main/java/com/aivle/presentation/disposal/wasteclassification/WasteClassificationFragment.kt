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
import com.loggi.core_util.extensions.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasteClassificationFragment : BaseDisposalFragment<FragmentWasteClassificationBinding>(
    R.layout.fragment_waste_classification) {

    private val viewModel: WasteClassificationViewModel by viewModels()
    private lateinit var smallCatListAdapter: SmallCategoryResultListAdapter
    private lateinit var loadingDialog: ProgressDialog

    private var selectedWasteSpec: WasteSpec?
        get() = activityViewModel.selectedWasteSpec
        set(value) { activityViewModel.selectedWasteSpec = value }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        // 다음 프래그먼트로 갔다가 다시 되돌아오는 경우
        if (activityViewModel.classificationResult == null) {
            viewModel.classifyWasteImage(activityViewModel.wasteImageLocalUri)
        }
    }

    private fun initView() {
        val wasteImageBitmap = BitmapUtil.decodeFile(activityViewModel.wasteImageLocalUri)
        binding.wasteImage.setImageBitmap(wasteImageBitmap)

        smallCatListAdapter = SmallCategoryResultListAdapter()
        binding.smallCategoryList.adapter = smallCatListAdapter

        binding.btnReselect.isEnabled = (selectedWasteSpec != null)
        binding.btnReselect.setOnClickListener {
            showSelectCategoryBottomSheet()
        }

        binding.btnNext.isEnabled = (selectedWasteSpec != null)
        binding.btnNext.setOnClickListener {
            if (selectedWasteSpec == null) {
                showToast("대분류와 소분류를 선택해주세요")
            } else {
                moveNext()
            }
        }

        loadingDialog = ProgressDialog(requireContext()).also {
            it.setContentView(R.layout.dialog_progress_ai)
            it.setCancelable(false)
        }

        log("initView(): ${activityViewModel.classificationResult}")
        // 다음 프래그먼트로 갔다가 다시 뒤돌아 왔을 경우
        if (activityViewModel.classificationResult != null) {
            showClassificationResult(activityViewModel.classificationResult!!)
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
        binding.btnReselect.isEnabled = true
        binding.noDetectMessage1.isVisible = true
        binding.largeCategoryResultList.isVisible = false
        binding.smallCategoryList.isVisible = false

        showToast("물체가 탐지되지 않았습니다. 사진을 재촬영하거나 직접 분류 선택을 해주세요.", Toast.LENGTH_LONG)
    }

    private fun showClassificationResult(classificationResult: ClassificationResult) {
        binding.btnReselect.isEnabled = true

        activityViewModel.classificationResult = classificationResult

        val firstLargeCategoryName = classificationResult.groups.firstOrNull()?.largeCategory?.categoryName
            ?: return
        binding.wasteName.text = firstLargeCategoryName
        binding.wasteName.isVisible = true
        binding.wasteNameSubLabel.isVisible = true

        // 이미지 대분류 결과 목록
        val largeResults = classificationResult.groups.map { it.largeCategory }
            .onEach {
                it.onClick = ::onChangedLargeCategorySelection
            }
        // 대분류가 선택되지 않은 경우 1등을 선택으로 함
        if (largeResults.none { it.isSelected }) {
            largeResults.first().isSelected = true
        }

        binding.largeCategoryResultList.isVisible = true
        binding.largeCategoryResultList.layoutManager = GridLayoutManager(requireContext(), largeResults.size)
        binding.largeCategoryResultList.adapter = LargeCategoryResultListAdapter().apply {
            submitList(largeResults)
        }

        // 이미지 소분류 결과 목록
        val smallResults = classificationResult.groups.find { it.largeCategory.isSelected }?.smallCategories
            ?: classificationResult.groups.first().smallCategories
        smallResults.forEach {
            it.onClick = ::onChangedSmallCategorySelection
        }
        // 소분류가 선택되지 않은 경우 percent > 0 인 항목으로 선택
        if (smallResults.none { it.isSelected }) {
            smallResults.find { it.percent != 0 }?.isSelected = true
        }

        binding.smallCategoryList.isVisible = true
        smallCatListAdapter.submitList(smallResults)

        // 소분류 결과까지 나온 경우 selectedSpec 에 저장
        val smallAiResult = smallResults.find { it.percent > 0 }
            ?: return
        selectedWasteSpec = smallAiResult.spec

        // 소분류 결과까지 나온 경우 "다음으로" 버튼 활성화
        binding.btnNext.isEnabled = true
    }

    private fun onChangedLargeCategorySelection(result: AiResult) {
        val smallCategories = activityViewModel.classificationResult!!.groups
            .find { it.largeCategory.index == result.index }!!
            .smallCategories
            .onEach {
                it.onClick = ::onChangedSmallCategorySelection
            }
        // 만약 선택된 소분류가 없는 경우 첫번째를 기본으로 선택해준다
//        if (smallCategories.none { it.isSelected }) {
//            smallCategories.first().isSelected = true
//        }
        selectedWasteSpec = smallCategories.find { it.isSelected }?.spec

        smallCatListAdapter.submitList(smallCategories)

        updateNextButtonEnabled()
    }

    private fun onChangedSmallCategorySelection(result: AiResult) {
        selectedWasteSpec = result.spec

        updateNextButtonEnabled()
    }

    private fun showSelectCategoryBottomSheet() {
        val allWasteSpecs = viewModel.wasteSpecTable
        if (allWasteSpecs.isNotEmpty()) {
            val bottomSheet = WasteCategoryBottomSheetFragment(allWasteSpecs) { selectedWasteSpec ->
                this.selectedWasteSpec = selectedWasteSpec
                updateNextButtonEnabled()
                moveNext()
            }
            bottomSheet.show(requireActivity().supportFragmentManager, "select-cat")
        }
    }

    private fun updateNextButtonEnabled() {
        val isEnabled = (selectedWasteSpec != null)
        binding.btnNext.isEnabled = isEnabled
    }

    private fun moveNext() {
        findNavController().navigate(R.id.action_wasteClassificationFragment_to_applyChoiceFragment)
    }
}