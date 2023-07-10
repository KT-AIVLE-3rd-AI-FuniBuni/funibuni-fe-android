package com.aivle.presentation.disposal.wasteclassification.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.BottomSheetWasteCategorySmallBinding
import com.loggi.core_util.extensions.log

class WasteCategorySmallFragment(
    private val parent: OnChildFragmentListener,
) : BaseFragment<BottomSheetWasteCategorySmallBinding>(R.layout.bottom_sheet_waste_category_small),
    WasteCategoryBottomSheetFragment.OnLargeCategorySelectedListener {

    private lateinit var listAdapter: WasteCategorySmallListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onSelected(wasteSpecs: List<WasteSpec>) {
        log("onSelected(): wasteSpecs=$wasteSpecs")
        binding.largeCategoryName.text = "대분류: ${wasteSpecs.first().large_category}"
        listAdapter.submitList(wasteSpecs.onEach {
            it.onClick2 = ::onClickSmallCategory
        })
    }

    private fun initView() {
        binding.listView.adapter = WasteCategorySmallListAdapter().also {
            listAdapter = it
        }
    }

    private fun onClickSmallCategory(wasteSpec: WasteSpec) {
        parent.onSelected(2, wasteSpec.waste_spec_id)
    }
}