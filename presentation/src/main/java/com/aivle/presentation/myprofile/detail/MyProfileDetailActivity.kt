package com.aivle.presentation.myprofile.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.aivle.domain.usecase.user.UserInfo
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation.databinding.ActivityMyProfileDetailBinding
import com.aivle.presentation.intro.intro.IntroActivity
import com.aivle.presentation.myprofile.detail.MyProfileDetailViewModel.Event
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MyProfileDetailActivity"

@AndroidEntryPoint
class MyProfileDetailActivity : BaseActivity<ActivityMyProfileDetailBinding>(R.layout.activity_my_profile_detail) {

    private val viewModel: MyProfileDetailViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { _, _, args ->
            binding.title.text = args?.getString("title")
        }

        setResult(RESULT_OK)
    }

    private fun initView() {
        binding.btnBack.setOnClickListener {
            val isRemainingStack = navController.popBackStack()
            if (!isRemainingStack) {
                finish()
            }
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.Failure -> {
                showToast(event.message)
            }
            is Event.MyProfile -> {
            }
            is Event.UpdateProfile -> {
            }
            is Event.SignOut -> {
                signOut()
            }
            is Event.Withdrawal -> {
                withdrawal()
            }
        }}
    }

    private fun signOut() {
        startIntroActivity()
    }

    private fun withdrawal() {
        startIntroActivity()
        showToast("Unsubscribed successfully.")
    }

    private fun startIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, MyProfileDetailActivity::class.java)
    }
}