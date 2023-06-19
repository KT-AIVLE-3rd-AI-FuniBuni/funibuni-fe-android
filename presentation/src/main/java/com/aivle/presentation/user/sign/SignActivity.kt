package com.aivle.presentation.user.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivitySignBinding
import com.aivle.presentation.user.firebase.FirebasePhoneAuth
import com.aivle.presentation.user.firebase.SmsRetrieveHelper
import com.google.firebase.FirebaseException
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SignActivity"

@AndroidEntryPoint
class SignActivity : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    private val viewModel: SignViewModel by viewModels()

    private val smsRetrieveHelper = SmsRetrieveHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        smsRetrieveHelper.init()
            .setOnSmsRetrieveCallback(OnMySmsRetrieverCallback())

        smsRetrieveHelper.requestHintPhone()

        initView()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        smsRetrieveHelper.handleActivityResult(requestCode, resultCode, data)
    }

    fun startSmsUserConsent(phoneNumber: String) {
        smsRetrieveHelper.startSmsUserConsent(phoneNumber)
    }

    private fun initView() {
        binding.header.btnBack.setOnClickListener {
            finish()
        }
    }

    inner class OnMySmsRetrieverCallback : SmsRetrieveHelper.OnSmsRetrieveCallback {
        override fun onPickedEmail(email: String) {
        }

        override fun onPickedPhone(phoneNumber: String) {
            Log.d(TAG, "onPickedPhone(): phoneNumber=$phoneNumber")
            viewModel.setPhoneNumber(phoneNumber)
        }

        override fun onSmsRetrieved(message: String, smsCode: String?) {
            Log.d(TAG, "onSmsRetrieved(): message=$message, smsCode=$smsCode")
            if (smsCode != null) {
                viewModel.setSmsCode(smsCode)
            }
        }
    }
}