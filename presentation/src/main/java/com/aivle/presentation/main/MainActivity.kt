package com.aivle.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aivle.domain.model.address.Address
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.ActivityMainBinding
import com.aivle.presentation.main.MainViewModel.Event
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()
        initView()
        handleViewModelEvent()
    }

    private fun initNavigation() {
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        binding.bottomNav.setupWithNavController(navController)
    }

    private fun initView() {
        binding.header.addressToggle.setOnClickListener {
            // TODO
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.AddressLoaded -> {
                setHeader(event.address)
            }
        }}
    }

    private fun setHeader(address: Address) {
        binding.header.address.text = address.roadAddress
        binding.header.addressToggle.isVisible = true
    }
}