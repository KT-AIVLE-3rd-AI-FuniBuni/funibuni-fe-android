package com.aivle.presentation.intro.intro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseActivity
import com.aivle.presentation.databinding.ActivityIntroBinding
import com.aivle.presentation.intro.sign.SignActivity
import com.aivle.presentation.main.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "IntroActivity"

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    private val viewModel: IntroViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            startMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initFirebaseApp()
        observeViewModel()
    }

    private fun initView() {
        binding.btnStart.setOnClickListener {
            startSignActivity()
        }
    }

    private fun initFirebaseApp() {
        // Firebase initialize
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance() // TODO
        )
    }

    private fun observeViewModel() {

    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startSignActivity() {
        signInLauncher.launch(SignActivity.getIntent(this))
    }
}