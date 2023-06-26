package com.aivle.presentation.intro.sign

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.Guideline
import androidx.core.widget.addTextChangedListener
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentSignUpInputNameBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SignUpInputNameFragment"

@AndroidEntryPoint
class SignUpInputNameFragment : BaseSignFragment<FragmentSignUpInputNameBinding>(R.layout.fragment_sign_up_input_name) {

    override val bottomButtonGuideLine: Guideline
        get() = binding.bottomButtonGuideline

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAnimation()
    }

    private fun initView() {
        binding.btnNext.setOnClickListener {
            signViewModel.userName = binding.edtUserName.text.toString()
            moveNextPage()
        }
        binding.edtUserName.addTextChangedListener {
            binding.btnNext.isEnabled = binding.edtUserName.text.isNotBlank()
        }
    }

    private fun initAnimation() {
        binding.guideMessage1.animateFadeInWithAfter {
            binding.guideMessage2.animateFadeInWithAfter {
                binding.edtLayoutUserName.animateFadeIn()
            }
        }
    }
}