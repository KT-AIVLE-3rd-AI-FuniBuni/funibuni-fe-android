package com.aivle.presentation.disposal.wasteclassification

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.BottomSheetWasteCategorySmallBinding

class WasteCategorySmallFragment() : BaseFragment<BottomSheetWasteCategorySmallBinding>(R.layout.bottom_sheet_waste_category_small) {

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }
}