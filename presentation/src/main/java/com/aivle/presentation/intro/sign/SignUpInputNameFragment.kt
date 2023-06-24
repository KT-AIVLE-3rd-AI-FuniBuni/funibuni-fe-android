package com.aivle.presentation.intro.sign

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSignUpInputNameBinding
import com.aivle.presentation.intro.sign.SignUpViewModel.Event
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SignUpInputNameFragment"

@AndroidEntryPoint
class SignUpInputNameFragment : BaseSignFragment<FragmentSignUpInputNameBinding>(R.layout.fragment_sign_up_input_name) {

    private val activityViewModel: SignViewModel by activityViewModels()
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView()")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView()")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated()")
        initView()
        initAnimation()
        handleEvent()
    }

    private fun initView() {
        binding.btnNext.setOnClickListener {
            activityViewModel.userName = binding.edtUserName.text.toString()
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