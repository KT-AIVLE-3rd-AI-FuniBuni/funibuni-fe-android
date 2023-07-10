package com.aivle.presentation.myprofile.privacyAndTerms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityPrivacyAndTermsBinding

class PrivacyAndTermsActivity : BaseActivity<ActivityPrivacyAndTermsBinding>(R.layout.activity_privacy_and_terms) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.header.title.text = "개인정보 처리방침"
        binding.header.btnBack.setOnClickListener {
            finish()
        }

        val webView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/funibuni_privacy_and_terms.html")
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, PrivacyAndTermsActivity::class.java)
    }
}