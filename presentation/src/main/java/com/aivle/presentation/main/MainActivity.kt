package com.aivle.presentation.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
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
    private lateinit var appBarConfiguration: AppBarConfiguration

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected(): $item")
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "onSupportNavigateUp()")
        return navController.navigateUp(appBarConfiguration)
    }

    private fun initNavigation() {
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        navController.addOnDestinationChangedListener { _, dest, args ->
            Log.d(TAG, "onDestinationChanged: ${dest.label}")
            if (dest.label == "HomeFragment") {
                updateHeader(isDark = true)
            } else {
                updateHeader(isDark = false)
            }
        }

        binding.bottomNav.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.disposalFragment, R.id.sharingPostListFragment, R.id.myProfileFragment)
        )

//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        val a = appBarConfiguration.topLevelDestinations

    }

    private fun updateHeader(isDark: Boolean) {
        if (isDark) {
            binding.header.address.setTextColor(Color.WHITE)
            binding.header.addressToggleIcon.imageTintList = ColorStateList.valueOf(Color.WHITE)
            binding.header.container.background = ColorDrawable(getColor(com.aivle.presentation_design.R.color.theme_color))
            binding.header.appLogo.setImageResource(R.drawable.funibuni_logo_white_half)
        } else {
            binding.header.address.setTextColor(Color.BLACK)
            binding.header.addressToggleIcon.imageTintList = ColorStateList.valueOf(Color.BLACK)
            binding.header.container.background = ColorDrawable(getColor(android.R.color.transparent))
            binding.header.appLogo.setImageResource(R.drawable.funibuni_logo)
        }
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