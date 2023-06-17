package com.aivle.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentHomeBinding
import com.aivle.presentation.user.sign.SignActivity
import com.aivle.presentation_design.interactive.ui.BottomUpDialog

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
        binding.btnTest.setOnClickListener {
            startActivity(Intent(requireActivity(), SignActivity::class.java))
        }
    }
}