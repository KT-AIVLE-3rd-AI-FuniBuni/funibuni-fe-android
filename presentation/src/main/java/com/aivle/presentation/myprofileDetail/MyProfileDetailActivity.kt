package com.aivle.presentation.myprofileDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.aivle.domain.model.user.User
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.common.showToast
import com.aivle.presentation.databinding.ActivityMyProfileDetailBinding
import com.aivle.presentation.intro.intro.IntroActivity
import com.aivle.presentation.myprofileDetail.MyProfileDetailViewModel.Event
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MyProfileDetailActivity"

@AndroidEntryPoint
class MyProfileDetailActivity : BaseActivity<ActivityMyProfileDetailBinding>(R.layout.activity_my_profile_detail) {

    private val viewModel: MyProfileDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()

        viewModel.loadMyProfileDetail()
    }

    private fun initView() {
        binding.btnSignOut.setOnClickListener {
            BottomUpDialog.Builder(supportFragmentManager)
                .title("로그아웃 하시겠습니까?")
                .positiveButton { viewModel.signOut() }
                .show()
        }
        binding.btnWithdrawal.setOnClickListener {
            BottomUpDialog.Builder(supportFragmentManager)
                .title("정말로 회원탈퇴를 하시겠습니까?")
                .subtitle("지금 탈퇴하시면 그동안의 기록이 모두 사라지게 됩니다.")
                .positiveButton { viewModel.withdrawal() }
                .show()
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
                showMyProfile(event.myProfile)
            }
            is Event.SignOut -> {
                signOut()
            }
            is Event.Withdrawal -> {
                withdrawal()
            }
        }}
    }

    private fun showMyProfile(user: User) {
        Log.d(TAG, user.toString())
    }

    private fun signOut() {
        startIntroActivity()
        showToast("Sign out completed.")
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