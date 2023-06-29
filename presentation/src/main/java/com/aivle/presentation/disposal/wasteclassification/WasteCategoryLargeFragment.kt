package com.aivle.presentation.disposal.wasteclassification

import android.os.Bundle
import android.view.View
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.BottomSheetWasteCategoryLargeBinding

class WasteCategoryLargeFragment : BaseFragment<BottomSheetWasteCategoryLargeBinding>(R.layout.bottom_sheet_waste_category_large) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.listView
    }
}