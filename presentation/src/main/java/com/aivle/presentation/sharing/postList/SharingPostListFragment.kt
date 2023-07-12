package com.aivle.presentation.sharing.postList

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentSharingPostListBinding
import com.aivle.presentation.main.MainActivity
import com.aivle.presentation.main.MainViewModel
import com.aivle.presentation.sharing.postDetail.SharingPostDetailActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingPostListFragment : BaseFragment<FragmentSharingPostListBinding>(R.layout.fragment_sharing_post_list) {

    private val activityViwModel: MainViewModel by activityViewModels()
    private val viewModel: SharingPostListViewModel by viewModels()
    private lateinit var listAdapter: SharingPostListAdapter

    private val postDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            activityViwModel.address?.let { address ->
                viewModel.loadSharingPosts(address.district)
            }
        }
    }

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

        binding.fabNewPost.setOnClickListener {
            showDialog()
        }
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.uiStateFlow.collect { uiState ->
            showPosts(uiState.data)

            uiState.message?.let(::showToast)
        }
    }

    private fun showPosts(posts: List<SharingPostItem>) {
        binding.noContent.isVisible = posts.isEmpty()
        listAdapter.submitList(posts.onEach {
            it.onClick = ::showPostDetail
        })
    }

    private fun showPostDetail(postId: Int) {
        val intent = SharingPostDetailActivity.getIntent(requireActivity(), postId)
        postDetailLauncher.launch(intent)
    }

    private fun showDialog() {
        BottomUpDialog.Builder(requireActivity())
            .title("나눔 게시물 등록을 위해 [버리기] 탭으로 이동합니다")
            .subtitle("나눔 게시판은 퍼니버니 AI와 함께 이미지 분류를 통해 진행됩니다")
            .confirmedButton {
                (requireActivity() as MainActivity).navigate(R.id.nav_disposal)
            }
            .show()
    }
}