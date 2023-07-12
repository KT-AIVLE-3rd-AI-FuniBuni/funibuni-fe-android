package com.aivle.presentation.myprofile.tab

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.aivle.domain.model.user.User
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentMyProfileBinding
import com.aivle.presentation.myprofile.applyDetail.WasteDisposalApplyDetailActivity
import com.aivle.presentation.myprofile.applyList.WasteDisposalApplyListActivity
import com.aivle.presentation.myprofile.applyList.WasteDisposalApplyListAdapter
import com.aivle.presentation.myprofile.detail.MyProfileDetailActivity
import com.aivle.presentation.myprofile.postList.MySharingPostListActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(R.layout.fragment_my_profile) {

    private val viewModel: MyProfileViewModel by viewModels()

    private lateinit var listAdapter: WasteDisposalApplyListAdapter

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.loadMyBuni()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeUiState()

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

        listAdapter = WasteDisposalApplyListAdapter()
        binding.recentListView.adapter = listAdapter
    }

    private fun observeUiState() = repeatOnStarted {
        viewModel.uiStateFlow.collect { (isLoading, message, data) ->
            data?.user?.let(::showUserInfo)
            data?.waste_applies?.let(::showRecentWasteApplies)
            message?.let(::showToast)
        }
    }

    private fun showUserInfo(user: User) {
        binding.name.text = user.nickname
    }

    private fun showRecentWasteApplies(applies: List<WasteDisposalApplyItem>) {
        binding.noContentView.isVisible = applies.isEmpty()

        applies.forEach {
            it.onClick = ::startWasteDisposalApplyDetailActivity
        }
        listAdapter.submitList(applies)
    }

    private fun startWasteDisposalApplyListActivity() {
        val intent = WasteDisposalApplyListActivity.getIntent(requireContext())
        activityLauncher.launch(intent)
    }

    private fun startMyProfileDetailActivity() {
        val intent = MyProfileDetailActivity.getIntent(requireContext())
        activityLauncher.launch(intent)
    }

    private fun startMySharingPostListActivity(postType: Int) {
        val intent = MySharingPostListActivity.getIntent(requireContext(), postType)
        activityLauncher.launch(intent)
    }

    private fun startWasteDisposalApplyDetailActivity(wasteDisposalApplyId: Int) {
        val intent = WasteDisposalApplyDetailActivity.getIntent(requireContext(), wasteDisposalApplyId)
        activityLauncher.launch(intent)
    }
}