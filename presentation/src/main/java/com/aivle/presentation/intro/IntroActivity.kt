package com.aivle.presentation.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityIntroBinding
import com.aivle.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    private val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        observerViewModel()

        viewModel.loadAddress()
    }

    private fun initView() {
        binding.btnInput.isVisible = false
        binding.btnInput.setOnClickListener {
            val address = binding.edtAddress.text.toString()
            if (address.isNotBlank()) {
                viewModel.setAddress(address)
            }
        }
    }

    private fun observerViewModel() {
        viewModel.isRegisteredAddress.observe(this) { isRegistered ->
            if (isRegistered) {
                startMainActivity()
            } else {
                binding.btnInput.isVisible = true
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}