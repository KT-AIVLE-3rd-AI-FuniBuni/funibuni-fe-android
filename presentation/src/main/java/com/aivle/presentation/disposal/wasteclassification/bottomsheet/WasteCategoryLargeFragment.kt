package com.aivle.presentation.disposal.wasteclassification.bottomsheet

import android.os.Bundle
import android.view.View
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.BottomSheetWasteCategoryLargeBinding

class WasteCategoryLargeFragment(
    private val wasteSpecs: List<WasteSpec>,
    private val parent: OnChildFragmentListener,
) : BaseFragment<BottomSheetWasteCategoryLargeBinding>(R.layout.bottom_sheet_waste_category_large) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        wasteSpecs.forEach {
            it.onClick1 = ::onClickLargeCategory
        }
        binding.listView.adapter = WasteCategoryListAdapter(wasteSpecs).apply {
            val wasteSpecGroups = wasteSpecs.distinctBy { it.top_category }

            submitList(wasteSpecGroups)
        }
    }

    private fun onClickLargeCategory(wasteSpec: WasteSpec) {
        parent.onSelected(1, wasteSpec.index_large_category)
    }
}