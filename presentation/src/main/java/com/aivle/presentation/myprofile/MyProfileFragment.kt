package com.aivle.presentation.myprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentMyProfileBinding
import com.aivle.presentation.myprofileDetail.MyProfileDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(R.layout.fragment_my_profile) {

    private val viewModel: MyProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnProfileDetail.setOnClickListener {
            startMyProfileDetailActivity()
        }
    }

    private fun startMyProfileDetailActivity() {
        startActivity(MyProfileDetailActivity.getIntent(requireContext()))
    }
}