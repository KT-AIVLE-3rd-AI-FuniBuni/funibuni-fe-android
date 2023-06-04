package com.aivle.presentation.main

import android.os.Bundle
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}