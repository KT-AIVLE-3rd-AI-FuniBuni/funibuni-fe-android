package com.aivle.presentation.sharing.postList

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.databinding.FragmentSharingPostListBinding
import com.aivle.presentation.main.MainViewModel
import com.aivle.presentation.sharing.postList.SharingPostListViewModel.Event
import com.aivle.presentation.sharing.postDetail.SharingPostDetailActivity
import com.aivle.presentation.util.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SharingPostListFragment : BaseFragment<FragmentSharingPostListBinding>(R.layout.fragment_sharing_post_list) {

    private val activityViwModel: MainViewModel by activityViewModels()
    private val viewModel: SharingPostListViewModel by viewModels()
    private lateinit var listAdapter: SharingPostListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleEvent()

        activityViwModel.address?.let {
            viewModel.loadSharingPosts(it.district)
        }
    }

    private fun initView() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            activityViwModel.address?.let {
                viewModel.loadSharingPosts(it.district)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        listAdapter = SharingPostListAdapter()
        binding.postList.adapter = listAdapter
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event ->
            binding.noContent.isVisible = event !is Event.LoadPosts.Success

            when (event) {
                is Event.None -> {}
                is Event.LoadPosts.Empty -> {
                }
                is Event.LoadPosts.Success -> {
                    val posts = event.posts.onEach {
                        it.onClick = ::onClickPost
                    }
                    listAdapter.submitList(posts)
                }
                is Event.LoadPosts.Failure -> {
                    showToast(event.message)
                }
            }
//            withContext(Dispatchers.Main) {
//                binding.swipeRefreshLayout.isRefreshing = false
//            }
        }
    }

    private fun onClickPost(postId: Int) {
        startActivity(
            SharingPostDetailActivity.getIntent(requireActivity(), postId)
        )
    }
}