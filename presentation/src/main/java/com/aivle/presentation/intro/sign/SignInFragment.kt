package com.aivle.presentation.intro.sign

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSignInBinding
import com.aivle.presentation.intro.sign.SignInViewModel.Event
import com.aivle.presentation.util.ext.dpToPixels
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.ui.FilterableMaterialAutoCompleteTextView
import com.aivle.presentation_design.interactive.ui.MySnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseSignFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    override val bottomButtonGuideLine: Guideline
        get() = binding.bottomButtonGuideline

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGuideMessageTextView()
        initView()
        handleViewModelEvent()
        handleActivityViewModelEvent()
    }

    private fun initGuideMessageTextView() {
        // 가이드 메시지 애니메이션
        binding.guideMessage1.animateFadeInWithAfter {
            binding.guideMessage2.animateFadeIn()
            binding.guidePrivacyPolicy.animateFadeInWithAfter {
                binding.edtLayoutPhoneNumber.animateFadeIn()
            }
        }

        // 개인정보처리방침 문구 링크 적용
        val privacyGuideText = "퍼니버니는 휴대폰 번호로 가입해요. 번호는 개인정보처리방침에 따라 안전하게 보관되며 어디에도 공개되지 않아요."
        val clickableText = "개인정보처리방침"
        val boldText = "안전하게 보관"

        val spannableString = SpannableString(privacyGuideText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPrivacyPolicyDialog()
            }
        }
        val boldSpan = StyleSpan(Typeface.BOLD)

        var startIndex = privacyGuideText.indexOf(clickableText)
        var endIndex = startIndex + clickableText.length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        startIndex = privacyGuideText.indexOf(boldText)
        endIndex = startIndex + boldText.length
        spannableString.setSpan(boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.guidePrivacyPolicy.text = spannableString
        binding.guidePrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showPrivacyPolicyDialog() {
        // 다이얼로그 커스텀뷰
        val dialogView = layoutInflater.inflate(R.layout.dialog_privacy_policy, null)
        val webView = dialogView.findViewById<WebView>(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/funibuni_privacy_and_terms.html")

        // 다이얼로그 생성
        AlertDialog.Builder(requireContext())
            .setTitle("개인정보 처리방침")
            .setView(dialogView)
            .setPositiveButton("확인", null)
            .show()

        // 다이얼로그 높이 지정
        dialogView.layoutParams = FrameLayout.LayoutParams(
            MATCH_PARENT, 400.dpToPixels(requireContext())
        )
    }

    private fun initView() {
        binding.edtPhoneNumber.addTextChangedListener(EditTextWatcher(binding.edtPhoneNumber))
        binding.edtPhoneAuthCode.addTextChangedListener(EditTextWatcher(binding.edtPhoneAuthCode))

        binding.btnAuth.setOnClickListener {
            if (!binding.edtLayoutPhoneAuthCode.isVisible) { // 인증 문자 받기
                val phoneNumber = binding.edtPhoneNumber.text.toString()

                startSmsUserConsent(phoneNumber)
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

    private fun handleViewModelEvent() = repeatOnStarted {
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
            is Event.RequestSms.OnVerificationCompleted -> {
                setSmsCode(event.smsCode)
            }
            is Event.RequestSms.OnVerificationFailed -> {
                showToast(event.message)
                enableRetryAuthButton(false)
                enablePhoneAuthButton(false)
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
                showToast("Event.SignIn.Succeed")
                signActivity.finishForSignIn()
            }
            is Event.SignIn.NotExistsUser -> {
                enableRetryAuthButton(false)
                binding.btnAuth.text = "다음으로"
                signViewModel.phoneNumber = viewModel.authenticatingPhoneNumber

                moveNextPage()
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

    private fun setSmsCode(smsCode: String) {
        binding.edtPhoneAuthCode.setText(smsCode)
        binding.edtPhoneAuthCode.setSelection(smsCode.length)
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

    private fun handleActivityViewModelEvent() = repeatOnStarted {
        signViewModel.dataEventFlow.collect { event -> when (event) {
            is SignViewModel.Event.PhoneNumber -> {
                val phoneNumber = event.value
                binding.edtPhoneNumber.setText(phoneNumber)
            }
            is SignViewModel.Event.SmsCode -> {
                val smsCode = event.value
                setSmsCode(smsCode)
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

            if (editText.id == binding.edtPhoneAuthCode.id && editText.isShowingError()) {
                editText.error = null
            }
        }
    }
}