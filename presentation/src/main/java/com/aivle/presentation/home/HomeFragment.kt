package com.aivle.presentation.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnDisposal.setOnClickListener {
            findNavController().navigate(R.id.action_global_disposalFragment)
        }
        binding.btnSharing.setOnClickListener {
            findNavController().navigate(R.id.action_global_sharingFragment)
        }
    }
}