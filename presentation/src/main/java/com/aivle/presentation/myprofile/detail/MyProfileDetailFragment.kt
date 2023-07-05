package com.aivle.presentation.myprofile.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aivle.domain.usecase.user.UserInfo
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentMyProfileDetailBinding
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import com.aivle.presentation.myprofile.detail.MyProfileDetailViewModel.Event
import com.aivle.presentation.util.ext.showToast

class MyProfileDetailFragment : BaseFragment<FragmentMyProfileDetailBinding>(R.layout.fragment_my_profile_detail) {

    private val activityViewModel: MyProfileDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()

        activityViewModel.loadMyProfileDetail()
    }

    private fun initView() {
        with (binding) {
            btnSignOut.setOnClickListener {
                BottomUpDialog.Builder(requireActivity())
                    .title("로그아웃 하시겠습니까?")
                    .positiveButton { activityViewModel.signOut() }
                    .show()
            }
            btnWithdrawal.setOnClickListener {
                BottomUpDialog.Builder(requireActivity())
                    .title("정말로 회원탈퇴를 하시겠습니까?")
                    .subtitle("지금 탈퇴하시면 그동안의 기록이 모두 사라지게 됩니다.")
                    .positiveButton { activityViewModel.withdrawal() }
                    .show()
            }
            btnFunibuniLink.setOnClickListener {

            }
            btnWasteSpec.setOnClickListener {

            }
            btnFreeDisposal.setOnClickListener {

            }
            btnPrivacyAndTerms.setOnClickListener {

            }
            btnOpenSourceLicense.setOnClickListener {

            }
            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_myProfileDetailFragment_to_updateMyProfileFragment)
            }
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        activityViewModel.eventFlow.collect { event -> when (event) {
            is Event.MyProfile -> {
                showUserInfo(event.userInfo)
            }
            is Event.Failure -> {
                showToast(event.message)
            }
            else -> {}
        }}
    }

    private fun showUserInfo(userInfo: UserInfo) {
        binding.user = userInfo.user
        binding.address = userInfo.address
    }
}