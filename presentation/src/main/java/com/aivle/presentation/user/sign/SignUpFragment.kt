package com.aivle.presentation.user.sign

import android.os.Bundle
import android.view.View
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentSignUpBinding

class SignUpFragment : BaseSignFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.guideMessage1.animateFadeInWithAfter {
            binding.guideMessage2.animateFadeInWithAfter {
                binding.edtLayoutPhoneNumber.animateFadeIn()
            }
        }
    }
}