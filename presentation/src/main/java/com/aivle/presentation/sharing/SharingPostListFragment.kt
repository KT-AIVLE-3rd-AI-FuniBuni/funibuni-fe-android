package com.aivle.presentation.sharing

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.common.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSharingPostListBinding
import com.aivle.presentation.sharing.SharingPostListViewModel.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingPostListFragment : BaseFragment<FragmentSharingPostListBinding>(R.layout.fragment_sharing_post_list) {

    private val viewModel: SharingPostListViewModel by viewModels()
    private lateinit var listAdapter: SharingPostListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleEvent()

        viewModel.loadSharingPosts()
    }

    private fun initView() {
        listAdapter = SharingPostListAdapter()
        binding.postList.adapter = listAdapter
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {}
            is Event.Success -> listAdapter.submitList(event.posts)
            is Event.Failure -> showToast(event.message)
        }}
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}