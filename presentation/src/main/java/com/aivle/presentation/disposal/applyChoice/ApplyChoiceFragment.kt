package com.aivle.presentation.disposal.applyChoice

import android.os.Bundle
import android.view.View
import com.aivle.presentation.R
import com.aivle.presentation._util.BitmapUtil
import com.aivle.presentation.databinding.FragmentApplyChoiceBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import java.text.DecimalFormat

private const val TAG = "ApplyChoiceFragment"

class ApplyChoiceFragment : BaseDisposalFragment<FragmentApplyChoiceBinding>(R.layout.fragment_apply_choice) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        val wasteSpec = activityViewModel.selectedWasteSpec ?: return
        val wasteImageBitmap = BitmapUtil.decodeFile(activityViewModel.wasteImageUri)
        val categoryName = "${wasteSpec.large_category} (${wasteSpec.small_category})"
        val wasteFee = DecimalFormat("#,###").format(wasteSpec.fee) + "원"

        binding.wasteImage.setImageBitmap(wasteImageBitmap)
        binding.wasteName.text = categoryName
        binding.disposalFee.text = wasteFee
    }

    private fun initListener() {
        binding.wasteImageCardView.setOnClickListener {

        }
        binding.sharingCardView.setOnClickListener {

        }
    }
}