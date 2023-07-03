package com.aivle.presentation.myprofile.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentUpdateMyProfileBinding
import com.aivle.presentation.myprofile.detail.MyProfileDetailViewModel

class UpdateMyProfileFragment : BaseFragment<FragmentUpdateMyProfileBinding>(R.layout.fragment_update_my_profile) {

    private val activityViewModel: MyProfileDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val nickname = activityViewModel.userInfo?.user?.nickname ?: return
        binding.edtNickname.setText(nickname)
        binding.btnComplete.setOnClickListener {
            activityViewModel.loadMyProfileDetail()
        }
    }
}