package com.aivle.presentation.intro.sign

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.Guideline
import androidx.core.widget.addTextChangedListener
import com.aivle.presentation.R
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSignUpInputAddressDetailBinding
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "SignUpInputAddressDetailFragment"

class SignUpInputAddressDetailFragment
    : BaseSignFragment<FragmentSignUpInputAddressDetailBinding>(R.layout.fragment_sign_up_input_address_detail) {

    override val bottomButtonGuideLine: Guideline
        get() = binding.bottomButtonGuideline

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleEvent()
    }

    private fun initView() {
        binding.edtAddressDetail.addTextChangedListener {
            binding.btnSignUp.isEnabled = (it?.isNotBlank() == true)
        }

        binding.btnSignUp.setOnClickListener {
            val addressDetail = binding.edtAddressDetail.text.toString()
            signViewModel.signUp(addressDetail)
        }
    }

    private fun handleEvent() = repeatOnStarted {
        signViewModel.addressEventFlow.collectLatest { address ->
            Log.d(TAG, "addressEventFlow.collectLatest: $address")
            val addressName = address.road_address!!.address_name
            val roadAddressName = address.road_address!!.address_name

            binding.addressName = addressName
            binding.roadAddressName = roadAddressName
        }
    }
}