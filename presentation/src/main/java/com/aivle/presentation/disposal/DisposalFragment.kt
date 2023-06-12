package com.aivle.presentation.disposal

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentDisposalBinding

class DisposalFragment : BaseFragment<FragmentDisposalBinding>(R.layout.fragment_disposal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnCamera.setOnClickListener {
            findNavController().navigate(R.id.action_disposalFragment_to_wasteClassificationFragment)
        }
    }
}