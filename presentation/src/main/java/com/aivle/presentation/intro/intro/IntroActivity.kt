package com.aivle.presentation.intro.intro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation.databinding.ActivityIntroBinding
import com.aivle.presentation.intro.intro.IntroViewModel.Event
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

        viewModel.checkRefreshTokenIfExistsSignIn()
//        Handler(Looper.getMainLooper()).postDelayed({
//            startMainActivity()
//        }, 1000)
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

    private fun observeViewModel() = repeatOnStarted {
        viewModel.eventFlow.collect { event ->
            binding.btnStart.isVisible = (event !is Event.SignIn.Succeed)

            when (event) {
                is Event.None -> {
                }
                is Event.RefreshToken.NotExists -> {
                }
                is Event.RefreshToken.Expired -> {
                    showToast("Token is expired")
                }
                is Event.RefreshToken.Invalid -> {
                    showToast("Token is invalid")
                }
                is Event.SignIn.Failure -> {
                    showToast(event.message)
                }
                is Event.SignIn.Succeed -> {
                    startMainActivity()
                }
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startSignActivity() {
        signInLauncher.launch(SignActivity.getIntent(this))
    }
}