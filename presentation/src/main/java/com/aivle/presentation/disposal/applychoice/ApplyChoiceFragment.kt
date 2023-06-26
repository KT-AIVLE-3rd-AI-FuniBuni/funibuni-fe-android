package com.aivle.presentation.disposal.applychoice

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.FragmentApplyChoiceBinding

class ApplyChoiceFragment : BaseFragment<FragmentApplyChoiceBinding>(R.layout.fragment_apply_choice) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnDisposal.setOnClickListener {
            signUp()
        }
        binding.btnSharing.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        findNavController().navigate(R.id.action_applyChoiceFragment_to_signUpFragment)
    }
}