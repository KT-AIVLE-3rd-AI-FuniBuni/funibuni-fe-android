package com.aivle.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aivle.domain.model.address.Address
import com.aivle.domain.model.util.DatetimeUtil
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.databinding.ActivityMainBinding
import com.aivle.presentation.main.MainViewModel.Event
import com.aivle.presentation.util.model.FuniBuniDate
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val today = FuniBuniDate.today()
        val other = FuniBuniDate(2023, 6, 3, 23)
        Log.d(TAG, "onCreate(): $today")
        Log.d(TAG, "onCreate(): $other")
        Log.d(TAG, "onCreate(): ${other - today}")

        initNavigation()
        initView()
        handleViewModelEvent()

        viewModel.loadAddress()
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
            is Event.None -> {
            }
            is Event.AddressLoaded -> {
                setHeader(event.address)
            }
        }}
    }

    private fun setHeader(address: Address) {
        Log.d(TAG, "setHeader(): $address")
        binding.header.address.text = "${address.city} ${address.district}"
        binding.header.addressToggle.isVisible = true
    }
}