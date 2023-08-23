package com.aivle.presentation.myprofile.applyDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.aivle.domain.model.waste.WasteDisposalApplyDetail
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityWasteDisposalApplyDetailBinding
import com.aivle.presentation.myprofile.applyDetail.WasteDisposalApplyDetailViewModel.Event
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.customView.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasteDisposalApplyDetailActivity : BaseActivity<ActivityWasteDisposalApplyDetailBinding>(R.layout.activity_waste_disposal_apply_detail) {

    private val viewModel: WasteDisposalApplyDetailViewModel by viewModels()

    private val wasteDisposalApplyId: Int?
        get() = intent.getIntExtra("wasteDisposalApplyId", -1).takeIf { it != -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()

        wasteDisposalApplyId?.let { applyId ->
            viewModel.loadApplyDetail(applyId)
        }
    }

    private fun initView() {
        binding.header.title.text = "배출 신청 상세정보"
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
                finish()
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showApplyDetail(detail: WasteDisposalApplyDetail) {
        binding.detail = detail
    }

    private fun showUnsupportedDialog() {
        BottomUpDialog.Builder(this)
            .title("아직 지원되지 않는 기능입니다")
            .confirmedButton()
            .show()
    }

    private fun showApplyCancellationDialog() {
        BottomUpDialog.Builder(this)
            .title("배출 신청을 정말 취소하시겠습니까?")
            .positiveButton {
                wasteDisposalApplyId?.let { applyId ->
                    viewModel.cancelWasteDisposalApply(applyId)
                }
            }
            .show()
    }

    companion object {

        const val WASTE_DISPOSAL_APPLY_ID = "wasteDisposalApplyId"

        fun getIntent(context: Context, wasteDisposalApplyId: Int) = Intent(
            context, WasteDisposalApplyDetailActivity::class.java
        ).apply {
            putExtra(WASTE_DISPOSAL_APPLY_ID, wasteDisposalApplyId)
        }
    }
}