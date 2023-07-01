package com.aivle.presentation.disposal.apply

import android.os.Bundle
import android.util.Log
import android.view.View
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentWasteDisposalApplyBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "WasteDisposalApplyFragment"

class WasteDisposalApplyFragment : BaseDisposalFragment<FragmentWasteDisposalApplyBinding>(R.layout.fragment_waste_disposal_apply) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindData()
    }

    private fun initView() {
        binding.btnApply.setOnClickListener {
            applyWasteDisposal()
        }
    }

    private fun bindData() {
        val classificationResult = activityViewModel.classificationResult ?: return
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return

        with (binding) {
            activityViewModel.classificationResult
            largeCategoryName.text = wasteSpec.large_category
            smallCategoryName.text = wasteSpec.small_category
            disposalFee.text = wasteSpec.feeString
        }
    }

    private fun applyWasteDisposal() {
        val date = binding.calendarView.date
        val timeString = binding.timeSelector.selectedTime
        val datetimeFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault()).format(date)
        val disposalDetailLocation = binding.edtDisposalDetailLocation.text.toString()
        val memo = binding.edtMemo.text.toString()
        Log.d(TAG, "applyWasteDisposal(): $datetimeFormat")
        Log.d(TAG, "applyWasteDisposal(): $timeString")
        Log.d(TAG, "applyWasteDisposal(): $disposalDetailLocation")
        Log.d(TAG, "applyWasteDisposal(): $memo")

        activityViewModel.applyWasteDisposal()
    }
}