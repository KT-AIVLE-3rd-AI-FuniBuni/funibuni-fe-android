package com.aivle.presentation.user.sign

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.common.dpToPixels
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSignInBinding
import com.aivle.presentation.user.sign.SignInViewModel.Event
import com.aivle.presentation_design.interactive.ui.FilterableMaterialAutoCompleteTextView
import com.aivle.presentation_design.interactive.ui.MySnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "SignInFragment"

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val activityViewModel: SignViewModel by activityViewModels()
    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleUiEvent()
        handleActivityViewModelEvent()
    }

    private fun initView() {
        binding.guideMessage1.animateFadeInWithAfter {
            binding.guideMessage2.animateFadeInWithAfter {
                binding.edtLayoutPhoneNumber.animateFadeIn()
            }
        }

        binding.edtPhoneNumber.addTextChangedListener(EditTextWatcher(binding.edtPhoneNumber))
        binding.edtPhoneAuthCode.addTextChangedListener(EditTextWatcher(binding.edtPhoneAuthCode))

        binding.btnAuth.setOnClickListener {
            if (!binding.edtLayoutPhoneAuthCode.isVisible) { // 인증 문자 받기
                val phoneNumber = binding.edtPhoneNumber.text.toString()
                viewModel.send(requireActivity(), phoneNumber)


            } else { // 인증하기
                val smsCode = binding.edtPhoneAuthCode.text.toString()
                viewModel.authenticateAndSignIn(requireActivity(), smsCode)
            }
        }

        binding.btnRetryPhoneAuth.setOnClickListener {
            enableRetryAuthButton(false)
            updateRetryAuthButtonTimer("00 : 00")

            val phoneNumber = binding.edtPhoneNumber.text.toString()
            viewModel.resend(requireActivity(), phoneNumber)
        }
    }

    private fun handleUiEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.RequestSms.FirstTry.Loading -> {
                binding.btnAuth.text = "인증하기"
                binding.btnAuth.isEnabled = false
                // 로딩중으로 변경
            }
            is Event.RequestSms.FirstTry.Started -> {
                showSnackBar("인증번호가 문자로 전송되었습니다 (최대 20초 소요)")
                showInputPhoneAuthCode()
                enableRetryAuthButton(true)
                enablePhoneAuthButton(false)
                // 로딩중 해제
                binding.edtPhoneAuthCode.text?.clear()
                binding.edtPhoneAuthCode.requestFocus()
            }
            is Event.RequestSms.Retry.Started -> {
                showSnackBar("인증번호가 문자로 전송되었습니다 (최대 20초 소요)")
                enableRetryAuthButton(true)
                // 로딩중 해제
                binding.edtPhoneAuthCode.text?.clear()
                binding.edtPhoneAuthCode.requestFocus()
            }
            is Event.RequestSms.UpdateTimer -> {
                if (binding.btnRetryPhoneAuth.isEnabled) {
                    updateRetryAuthButtonTimer(event.remainingTime)
                }
            }
            is Event.RequestSms.IncorrectCode -> {
                binding.edtPhoneAuthCode.error = "인증번호가 맞지 않습니다. 다시 확인해주세요."
            }
            is Event.RequestSms.Timeout -> {
                showSnackBar("시간이 초과되었습니다. 다시 인증해주세요.")
                enablePhoneAuthButton(false)
            }
            is Event.RequestSms.Exception -> {
                showToast(event.message)
                enableRetryAuthButton(false)
                enablePhoneAuthButton(false)
            }
            is Event.SignIn.Succeed -> {
                showToast("SignInSucceed")
                enableRetryAuthButton(false)
                enablePhoneAuthButton(false)
            }
            is Event.SignIn.NotExistsUser -> {
                showToast("SignInFailure")
                enableRetryAuthButton(false)
                binding.btnAuth.text = "다음"
            }
            is Event.SignIn.Exception -> {
                showToast(event.message)
            }
            else -> {}
        }}
    }

    private fun showInputPhoneAuthCode() {
        binding.edtLayoutPhoneAuthCode.animateSettle(requireContext())
        binding.btnRetryPhoneAuth.animateSettle(requireContext())
    }

    private fun enableRetryAuthButton(isEnabled: Boolean) {
        binding.btnRetryPhoneAuth.isEnabled = isEnabled
    }

    private fun updateRetryAuthButtonTimer(remainingTime: String) {
        binding.btnRetryPhoneAuth.text = "인증 문자 다시 받기 (${remainingTime})"
    }

    private fun enablePhoneAuthButton(isEnabled: Boolean) {
        binding.btnAuth.isEnabled = isEnabled
    }

    private fun showSnackBar(message: String) {
        MySnackBar.Builder(binding.root)
            .setMessage(message)
            .setAnchorView(binding.btnAuth)
            .show()
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleActivityViewModelEvent() = repeatOnStarted {
        activityViewModel.dataEventFlow.collect { event -> when (event) {
            is SignViewModel.Event.PhoneNumber -> {
                Log.d(TAG, "SignViewModel.Event.PhoneNumber")
                binding.edtPhoneNumber.setText(event.value)
            }
            is SignViewModel.Event.SmsCode -> {
                Log.d(TAG, "SignViewModel.Event.SmsCode")
                binding.edtPhoneAuthCode.setText(event.value)
                binding.edtPhoneAuthCode.setSelection(event.value.length)
            }
        }}
    }

    private inner class EditTextWatcher(
        private val editText: FilterableMaterialAutoCompleteTextView
    ) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isEnabled = if (!binding.btnRetryPhoneAuth.isVisible) { // 문자 요청 전
                binding.edtPhoneNumber.text.toString().length == 13
            } else { // 문자 요청 후
                viewModel.isSameAuthenticatingPhoneNumber(binding.edtPhoneNumber.text.toString())
                        && binding.edtPhoneAuthCode.text.toString().length == 6
            }
            enablePhoneAuthButton(isEnabled)

            Log.d(TAG, "onTextChanged(): ${editText.id == binding.edtPhoneAuthCode.id}, ${editText.error}\"")
            if (editText.id == binding.edtPhoneAuthCode.id && editText.isShowingError()) {
                editText.error = null
            }
        }
    }
}

private fun View.animateSettle(context: Context) {
    this.animate()
        .translationY(38f.dpToPixels(context))
        .alpha(1f)
        .setDuration(500L)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withStartAction { this.isVisible = true }
        .start()
}

private fun View.animateFadeIn() {
    animateFadeInWithAfter(null)
}

private fun View.animateFadeInWithAfter(action: (() -> Unit)?) {
    visibility = View.VISIBLE
    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 0.2f, 1f)
    val translate = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -100f, 0f)

    ObjectAnimator.ofPropertyValuesHolder(this, alpha, translate).apply {
        duration = 500
        interpolator = AccelerateDecelerateInterpolator()
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                action?.invoke()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }.start()
}