package com.aivle.presentation.disposal.applyDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aivle.domain.model.waste.WasteDisposalApplyDetail
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentWasteDisposalApplyDetailBinding
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.disposal.applyDetail.WasteDisposalApplyDetailViewModel.Event
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WasteDisposalApplyDetailFragment"

@AndroidEntryPoint
class WasteDisposalApplyDetailFragment
    : BaseFragment<FragmentWasteDisposalApplyDetailBinding>(R.layout.fragment_waste_disposal_apply_detail) {

    private val viewModel: WasteDisposalApplyDetailViewModel by viewModels()

    private val wasteDisposalApplyId: Int?
        get() = arguments?.getInt("wasteDisposalApplyId", -1)?.takeIf { it != -1 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        wasteDisposalApplyId?.let { applyId ->
            viewModel.loadApplyDetail(applyId)
        }
    }

    private fun initView() {
        binding.btnApplyCancel.setOnClickListener {
            wasteDisposalApplyId?.let { applyId ->
                viewModel.cancelWasteDisposalApply(applyId)
            }
        }
        binding.btnPay.setOnClickListener {
            showUnsupportedDialog()
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.LoadApplyDetail.Success -> {
                showApplyDetail(event.detail)
            }
            is Event.CancelApply.Success -> {
                showApplyCancellationDialog()
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showApplyDetail(detail: WasteDisposalApplyDetail) {
        binding.detail = detail

        Glide.with(requireContext())
            .load(detail.image_url)
            .centerCrop()
            .error(R.drawable.placeholder_1440)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.wasteImage)
    }

    private fun showUnsupportedDialog() {
        BottomUpDialog.Builder(requireActivity())
            .title("아직 지원되지 않는 기능입니다")
            .confirmedButton()
            .show()
    }

    private fun showApplyCancellationDialog() {
        BottomUpDialog.Builder(requireActivity())
            .title("배출 신청을 정말 취소하시겠습니까?")
            .positiveButton {
                closeFragmentOnCancellation()
            }
            .show()
    }

    private fun closeFragmentOnCancellation() {
        requireActivity().finish()
    }
}