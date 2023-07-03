package com.aivle.presentation.myprofile.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aivle.domain.usecase.user.UserInfo
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentMyProfileBinding
import com.aivle.presentation.myprofile.applyList.WasteDisposalApplyListActivity
import com.aivle.presentation.myprofile.detail.MyProfileDetailActivity
import com.aivle.presentation.myprofile.postList.MySharingPostListActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.myprofile.tab.MyProfileViewModel.Event
import com.aivle.presentation.util.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(R.layout.fragment_my_profile) {

    private val viewModel: MyProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        viewModel.loadMyBuni()
    }

    private fun initView() {
        binding.btnProfileDetail.setOnClickListener {
            startMyProfileDetailActivity()
        }
        binding.btnDisposalList.setOnClickListener {
            startWasteDisposalApplyListActivity()
        }
        binding.btnSharingList.setOnClickListener {
            startMySharingPostListActivity(MySharingPostListActivity.SHARING_POSTS)
        }
        binding.btnFavoriteList.setOnClickListener {
            startMySharingPostListActivity(MySharingPostListActivity.FAVORITE_POSTS)
        }
        binding.btnActivities.setOnClickListener {
            startMySharingPostListActivity(MySharingPostListActivity.ACTIVITIES)
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.LoadMyBuni.Success -> {
                val userInfo = event.data
                showUserInfo(userInfo)
                showRecentList()
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showUserInfo(userInfo: UserInfo) {
        binding.name.text = userInfo.user.nickname
    }

    private fun showRecentList() {

    }

    private fun startWasteDisposalApplyListActivity() {
        val intent = WasteDisposalApplyListActivity.getIntent(requireContext())
        startActivity(intent)
    }

    private fun startMyProfileDetailActivity() {
        val intent = MyProfileDetailActivity.getIntent(requireContext())
        startActivity(intent)
    }

    private fun startMySharingPostListActivity(postType: Int) {
        val intent = MySharingPostListActivity.getIntent(requireContext(), postType)
        startActivity(intent)
    }
}