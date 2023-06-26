package com.aivle.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aivle.presentation.R
import com.aivle.presentation._base.BaseActivity
import com.aivle.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()
        initHeader()
    }

    private fun initNavigation() {
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d(TAG, "onDestinationChanged(): $destination")
            Log.d(TAG, "onDestinationChanged(): $arguments")
            Log.d(TAG, "onDestinationChanged(): ${destination.id}")
            Log.d(TAG, "onDestinationChanged(): ${destination.label}")
            Log.d(TAG, "onDestinationChanged(): ${destination.displayName}")

            if (arguments?.getBoolean("IsNavRootFragment") == true) {
                binding.header.btnBack.isVisible = false
                binding.header.title.isVisible = false
                binding.header.btnAddress.isVisible = true

                binding.header.title.text = ""
            } else {
                binding.header.btnBack.isVisible = true
                binding.header.title.isVisible = true
                binding.header.btnAddress.isVisible = false

                binding.header.title.text = arguments?.getString("HeaderTitle") ?: ""
            }
        }
    }

    private fun initHeader() {
        binding.header.address.text = "송파구"
        binding.header.btnAddress.setOnClickListener {  }
    }
}