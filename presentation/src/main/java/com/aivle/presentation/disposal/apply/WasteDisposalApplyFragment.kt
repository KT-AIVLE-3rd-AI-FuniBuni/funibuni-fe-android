package com.aivle.presentation.disposal.apply

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aivle.domain.model.address.Address
import com.aivle.presentation.R
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation.databinding.FragmentWasteDisposalApplyBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import com.aivle.presentation.util.common.DatetimeUtil
import com.aivle.presentation.disposal.apply.WasteDisposalApplyViewModel.Event
import com.aivle.presentation.util.model.FuniBuniDate
import com.loggi.core_util.extensions.log
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

private const val TAG = "WasteDisposalApplyFragment"

@AndroidEntryPoint
class WasteDisposalApplyFragment : BaseDisposalFragment<FragmentWasteDisposalApplyBinding>(R.layout.fragment_waste_disposal_apply) {

    private val viewModel: WasteDisposalApplyViewModel by viewModels()
    private var date: FuniBuniDate = FuniBuniDate.today()

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
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return

        with (binding) {
            largeCategoryName.text = wasteSpec.large_category
            smallCategoryName.text = wasteSpec.small_category
            disposalFee.text = wasteSpec.feeString
        }
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            log("setOnDateChangeListener: $year, $month, $dayOfMonth")
            date = FuniBuniDate(year, month, dayOfMonth)
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
                moveApplyDetailFragment(event.applyId)
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
        val dateString = date.toDateString()
        val (hour, minute) = binding.timeSelector.selectedTime.split(":")
        val datetimeString = FuniBuniDate(date.year, date.month, date.day, hour.toInt(), minute.toInt()).toServerFormat2()


//        val datetimeString = DatetimeUtil.toServer(dateString, hour.toInt(), minute.toInt(), 0)
        val disposalDetailLocation = binding.edtDisposalDetailLocation.text.toString()
        val memo = binding.edtMemo.text.toString()
            .ifBlank { "요청 사항 없음" }

        val classificationResult = activityViewModel.classificationResult ?: return
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return
        viewModel.applyWasteDisposal(classificationResult, wasteSpec, datetimeString, disposalDetailLocation, memo)
    }

    private fun moveApplyDetailFragment(applyId: Int) {
        findNavController().navigate(R.id.action_wasteDisposalApplyFragment_to_wasteDisposalApplyDetailFragment, bundleOf(
            "wasteDisposalApplyId" to applyId
        ))
    }
}