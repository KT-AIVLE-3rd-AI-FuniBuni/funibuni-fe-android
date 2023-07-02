package com.aivle.presentation.disposal.apply

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.aivle.domain.model.address.Address
import com.aivle.presentation.R
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.common.showToast
import com.aivle.presentation.databinding.FragmentWasteDisposalApplyBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import com.aivle.presentation.util.common.DatetimeUtil
import com.aivle.presentation.disposal.apply.WasteDisposalApplyViewModel.Event
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WasteDisposalApplyFragment"

@AndroidEntryPoint
class WasteDisposalApplyFragment : BaseDisposalFragment<FragmentWasteDisposalApplyBinding>(R.layout.fragment_waste_disposal_apply) {

    private val viewModel: WasteDisposalApplyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindData()
        handleViewModelEvent()

        viewModel.loadAddress()
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
            largeCategoryName.text = wasteSpec.large_category
            smallCategoryName.text = wasteSpec.small_category
            disposalFee.text = wasteSpec.feeString
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.AddressLoaded -> {
                showAddress(event.address)
            }
            is Event.Apply.Success -> {
                activityViewModel.completeApplication(event.applyId)
            }
            is Event.Apply.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showAddress(address: Address) {
        binding.address.text = "${address.roadAddress}\n${address.detail}"
        val disposalLocation = address.disposalLocation
        if (disposalLocation.isNotBlank()) {
            binding.edtDisposalDetailLocation.setText(disposalLocation)
            binding.edtDisposalDetailLocation.setSelection(disposalLocation.length)
        }
    }

    private fun applyWasteDisposal() {
        val date = binding.calendarView.date
        val dateString = DatetimeUtil.dateString(date)
        val (hour, minute) = binding.timeSelector.selectedTime.split(":")

        val datetimeString = DatetimeUtil.toServer(dateString, hour.toInt(), minute.toInt(), 0)
        val disposalDetailLocation = binding.edtDisposalDetailLocation.text.toString()
        val memo = binding.edtMemo.text.toString()
            .ifBlank { "요청 사항 없음" }

        val document = activityViewModel.classificationResult ?: return
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return

        viewModel.applyWasteDisposal(document, wasteSpec, datetimeString, disposalDetailLocation, memo)
    }

    private fun moveApplyDetailFragment(applyId: Int) {
        Log.d(TAG, "moveApplyDetailFragment(): applyId=$applyId")
    }
}