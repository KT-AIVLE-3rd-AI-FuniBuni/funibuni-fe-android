package com.aivle.presentation.intro.sign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation.util.common.KeyboardHeightProvider
import com.aivle.presentation.databinding.ActivitySignBinding
import com.aivle.presentation.intro.firebase.SmsRetrieveHelper
import com.aivle.presentation.intro.sign.SignViewModel.SignUpEvent
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SignActivity"

@AndroidEntryPoint
class SignActivity : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    private val viewModel: SignViewModel by viewModels()
    private val smsRetrieveHelper = SmsRetrieveHelper(this)

    private lateinit var viewPagerChangeCallback: OnViewPagerChangeCallback
    lateinit var keyboardHeightProvider: KeyboardHeightProvider

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            moveBackPage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        smsRetrieveHelper.init()
            .setOnSmsRetrieveCallback(OnMySmsRetrieverCallback())

        smsRetrieveHelper.requestHintPhone()
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        keyboardHeightProvider = KeyboardHeightProvider(this)

        initView()
        handleEvent()
    }

    override fun onResume() {
        super.onResume()
        keyboardHeightProvider.onResume()
    }

    override fun onPause() {
        super.onPause()
        keyboardHeightProvider.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardHeightProvider.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerChangeCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        smsRetrieveHelper.handleActivityResult(requestCode, resultCode, data)
    }

    fun moveBackPage() {
        if (keyboardHeightProvider.isShowingKeyboard) {
            keyboardHeightProvider.hideKeyboard()
        }
        if (binding.viewPager.currentItem == 0) {
            requestFinish()
        } else {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }
    }

    fun moveNextPage() {
        if (keyboardHeightProvider.isShowingKeyboard) {
            keyboardHeightProvider.hideKeyboard()
        }
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    fun startSmsUserConsent(phoneNumber: String) {
        smsRetrieveHelper.startSmsUserConsent(phoneNumber)
    }

    fun finishForSignIn() {
        finishWithResult()
    }

    fun finishForSignUp() {
        finishWithResult()
    }

    private fun finishWithResult() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun initView() {
        binding.header.title.isVisible = true
        binding.header.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewPagerChangeCallback = OnViewPagerChangeCallback()
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = SignPageAdapter(this)
        binding.viewPager.registerOnPageChangeCallback(viewPagerChangeCallback)
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.signUpEventFlow.collect { event -> when (event) {
            is SignUpEvent.Success -> {
                finishForSignUp()
            }
            is SignUpEvent.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun requestFinish() {
        BottomUpDialog.Builder(this)
            .title("여기서 그만하고 나가시겠습니까?")
            .positiveButton("계속하기")
            .negativeButton("안할래요") {
                finish()
            }
            .show()
    }

    inner class OnMySmsRetrieverCallback : SmsRetrieveHelper.OnSmsRetrieveCallback {
        override fun onPickedEmail(email: String) {
        }

        override fun onPickedPhone(phoneNumber: String) {
            Log.d(TAG, "onPickedPhone(): phoneNumber=$phoneNumber")
            viewModel.sendPhoneNumber(phoneNumber)
        }

        override fun onSmsRetrieved(message: String, smsCode: String?) {
            Log.d(TAG, "onSmsRetrieved(): message=$message, smsCode=$smsCode")
            if (smsCode != null) {
                viewModel.sendSmsCode(smsCode)
            }
        }
    }

    inner class OnViewPagerChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.header.title.text = when (position) {
                SIGN_IN -> "로그인"
                SIGN_UP_INPUT_NAME,
                SIGN_UP_INPUT_ADDRESS,
                SIGN_UP_INPUT_ADDRESS_DETAIL -> "회원가입"
                else -> throw IndexOutOfBoundsException("position=$position")
            }
        }
    }

    class SignPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment = when (position) {
            SIGN_IN -> SignInFragment()
            SIGN_UP_INPUT_NAME -> SignUpInputNameFragment()
            SIGN_UP_INPUT_ADDRESS -> SignUpInputAddressFragment()
            SIGN_UP_INPUT_ADDRESS_DETAIL -> SignUpInputAddressDetailFragment()
            else -> throw IndexOutOfBoundsException("position=$position")
        }
    }

    companion object {
        private const val SIGN_IN = 0
        private const val SIGN_UP_INPUT_NAME = 1
        private const val SIGN_UP_INPUT_ADDRESS = 2
        private const val SIGN_UP_INPUT_ADDRESS_DETAIL = 3

        fun getIntent(context: Context): Intent {
            return Intent(context, SignActivity::class.java)
        }
    }
}