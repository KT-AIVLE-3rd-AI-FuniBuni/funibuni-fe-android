package com.aivle.presentation.disposal.wasteclassification.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.BottomSheetWasteCategorySmallBinding

private const val TAG = "WasteCategorySmallFragment"

class WasteCategorySmallFragment(
    private val parent: OnChildFragmentListener,
) : BaseFragment<BottomSheetWasteCategorySmallBinding>(R.layout.bottom_sheet_waste_category_small),
    WasteCategoryBottomSheetFragment.OnLargeCategorySelectedListener {

    private lateinit var listAdapter: WasteCategorySmallListAdapter
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onSelected(wasteSpecs: List<WasteSpec>) {
        Log.d(TAG, "onSelected(): wasteSpecs=$wasteSpecs")
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