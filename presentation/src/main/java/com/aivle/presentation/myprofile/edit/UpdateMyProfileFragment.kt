package com.aivle.presentation.myprofile.edit

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentUpdateMyProfileBinding
import com.aivle.presentation.myprofile.detail.MyProfileDetailViewModel
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.myprofile.detail.MyProfileDetailViewModel.Event

class UpdateMyProfileFragment : BaseFragment<FragmentUpdateMyProfileBinding>(R.layout.fragment_update_my_profile) {

    private val activityViewModel: MyProfileDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleViewModelEvent()
    }

    private fun initView() {
        val nickname = activityViewModel.userInfo?.user?.nickname ?: return
        binding.edtNickname.setText(nickname)
        binding.edtNickname.addTextChangedListener {
            val length = it?.length ?: return@addTextChangedListener
            binding.btnComplete.isEnabled = length > 0
        }
        binding.edtNickname.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateMyProfile()
            }
            false
        }
        binding.btnComplete.setOnClickListener {
            updateMyProfile()
        }
    }

    private fun updateMyProfile() {
        val newNickname = binding.edtNickname.text.toString()
        if (newNickname.isNotBlank()) {
            activityViewModel.updateMyProfile(newNickname)
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        activityViewModel.eventFlow.collect { event ->
            when (event) {
                is Event.MyProfile -> {
                    binding.edtNickname.setText(event.userInfo.user.nickname)
                }
                is Event.UpdateProfile -> {
                    findNavController().popBackStack()
                }
                else -> {}
            }
        }
    }
}