package com.aivle.presentation.home

import android.os.Bundle
import android.view.View
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentHomeBinding
import com.aivle.presentation.main.MainActivity
import com.aivle.presentation.searchWasteSpec.SearchWasteSpecActivity
import com.aivle.presentation.util.ext.showToast

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
            navigate(R.id.nav_disposal)
        }
        binding.btnSharing.setOnClickListener {
            navigate(R.id.nav_sharing)
        }
        binding.btnArCamera.setOnClickListener {
            showToast("아직 실험중인 기능입니다😂")
        }
    }

    private fun startSearchWasteSpec() {
        startActivity(SearchWasteSpecActivity.getIntent(requireContext()))
    }

    private fun navigate(menuItemId: Int) {
        (requireActivity() as MainActivity).navigate(menuItemId)
    }
}