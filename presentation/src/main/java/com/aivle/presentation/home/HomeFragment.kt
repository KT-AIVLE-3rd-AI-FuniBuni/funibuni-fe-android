package com.aivle.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.collection.forEach
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentHomeBinding
import com.aivle.presentation.searchWasteSpec.SearchWasteSpecActivity

private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnSearchWaste.setOnClickListener {
            startSearchWasteSpec()
        }
        binding.btnDisposal.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nav_disposal)
        }
        binding.btnSharing.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nav_sharing)
        }
    }

    private fun startSearchWasteSpec() {
        startActivity(SearchWasteSpecActivity.getIntent(requireContext()))
    }
}