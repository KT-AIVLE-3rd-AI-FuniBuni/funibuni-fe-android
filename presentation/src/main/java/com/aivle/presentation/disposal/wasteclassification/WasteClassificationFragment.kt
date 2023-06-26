package com.aivle.presentation.disposal.wasteclassification

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.FragmentWasteClassificationBinding

class WasteClassificationFragment : BaseFragment<FragmentWasteClassificationBinding>(R.layout.fragment_waste_classification) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_wasteClassificationFragment_to_applyChoiceFragment)
        }
    }
}