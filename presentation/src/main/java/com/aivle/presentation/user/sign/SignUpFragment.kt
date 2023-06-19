package com.aivle.presentation.user.sign

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSignUpBinding
import com.aivle.presentation.user.sign.SignUpViewModel.Event
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SignUpFragment"

@AndroidEntryPoint
class SignUpFragment : BaseSignFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val activityViewModel: SignViewModel by activityViewModels()
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initAnimation()
        handleEvent()
    }

    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            val phoneNumber = activityViewModel.phoneNumber
            val userName = binding.edtUserName.text.toString()
            viewModel.signUp(phoneNumber, userName)
        }
        binding.edtUserName.addTextChangedListener {
            binding.btnSignUp.isEnabled = binding.edtUserName.text.isNotBlank()
        }
    }

    private fun initAnimation() {
        binding.guideMessage1.animateFadeInWithAfter {
            binding.guideMessage2.animateFadeInWithAfter {
                binding.edtLayoutUserName.animateFadeIn()
            }
        }
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {}
            is Event.Success -> {
                Log.d(TAG, "handleEvent(): Event.Success")
                (requireActivity() as SignActivity).finish()
            }
            is Event.Failure.Error -> {
                showToast("Server Error")
            }
            is Event.Failure.Exception -> {
                showToast(event.message)
            }
        }}
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}