package com.aivle.presentation.intro.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivitySignBinding
import com.aivle.presentation.intro.firebase.SmsRetrieveHelper
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SignActivity"

@AndroidEntryPoint
class SignActivity : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    private val viewModel: SignViewModel by viewModels()
    private val smsRetrieveHelper = SmsRetrieveHelper(this)

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        smsRetrieveHelper.init()
            .setOnSmsRetrieveCallback(OnMySmsRetrieverCallback())

        smsRetrieveHelper.requestHintPhone()

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        initView()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        smsRetrieveHelper.handleActivityResult(requestCode, resultCode, data)
    }

    fun moveNextPage() {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    fun startSmsUserConsent(phoneNumber: String) {
        smsRetrieveHelper.startSmsUserConsent(phoneNumber)
    }

    private fun initView() {
        binding.header.btnBack.setOnClickListener {
            backPressed()
        }
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = SignPageAdapter(this)
    }

    private fun backPressed() {
        if (binding.viewPager.currentItem == 0) {
            requestFinish()
        } else {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }
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

    class SignPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> SignInFragment()
            1 -> SignUpInputNameFragment()
            2 -> SignUpInputAddressFragment()
            else -> throw IndexOutOfBoundsException("position=$position")
        }
    }
}