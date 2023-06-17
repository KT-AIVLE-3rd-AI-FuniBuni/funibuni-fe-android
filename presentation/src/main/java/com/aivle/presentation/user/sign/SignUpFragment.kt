package com.aivle.presentation.user.sign

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel2 by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleEvent()
    }

    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            viewModel.signUp(
                binding.edtUserId.text.toString(),
                binding.edtPassword.text.toString(),
                binding.edtUserName.text.toString(),
                binding.edtPhoneNumber.text.toString(),
            )
        }

    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is SignUpViewModel2.Event.Success -> showToast("Success")
                is SignUpViewModel2.Event.Error.DuplicateUserID -> showToast("DuplicateIdError")
                is SignUpViewModel2.Event.Error.DuplicatePhoneNumber -> showToast("DuplicatePhoneNumberError")
                is SignUpViewModel2.Event.ShowToastError -> showToast(event.message)
            }
        }
    }

    private fun showToast(message: String?) {
        if (message == null) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}