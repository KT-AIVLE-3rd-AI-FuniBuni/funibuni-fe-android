package com.aivle.presentation.disposal

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.FragmentDisposalBinding

class DisposalFragment : BaseFragment<FragmentDisposalBinding>(R.layout.fragment_disposal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnCamera.setOnClickListener {
            startActivity(Intent(requireContext(), DisposalApplyActivity::class.java))
        }
    }
}