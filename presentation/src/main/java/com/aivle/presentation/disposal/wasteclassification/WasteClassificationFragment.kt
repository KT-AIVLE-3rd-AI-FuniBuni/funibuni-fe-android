package com.aivle.presentation.disposal.wasteclassification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation._util.BitmapUtil
import com.aivle.presentation.databinding.FragmentWasteClassificationBinding
import com.aivle.presentation.disposal.base.BaseDisposalFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WasteClassificationFragment"

@AndroidEntryPoint
class WasteClassificationFragment : BaseDisposalFragment<FragmentWasteClassificationBinding>(
    R.layout.fragment_waste_classification) {

    private val viewModel: WasteClassificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewModel.classifyWasteImage(parentViewModel.wasteImageUri)
    }

    private fun initView() {
        val wasteImageBitmap = BitmapUtil.decodeFile(parentViewModel.wasteImageUri)
        binding.wasteImage.setImageBitmap(wasteImageBitmap)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_wasteClassificationFragment_to_applyChoiceFragment)
        }
    }
}