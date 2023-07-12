package com.aivle.presentation.myprofile.postList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivityMySharingPostListBinding
import com.aivle.presentation.sharing.postDetail.SharingPostDetailActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySharingPostListActivity : BaseActivity<ActivityMySharingPostListBinding>(R.layout.activity_my_sharing_post_list) {

    private val viewModel: MySharingPostListViewModel by viewModels()

    private lateinit var listAdapter: MySharingPostListAdapter

    private val postType: Int
        get() = intent.getIntExtra(POST_TYPE, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()

        when (postType) {
            SHARING_POSTS -> viewModel.loadMySharingPosts()
            FAVORITE_POSTS -> viewModel.loadMyFavoritePosts()
            ACTIVITIES -> viewModel.loadMyActivities()
        }
    }

    private fun initView() {
        binding.header.title.text = when (postType) {
            SHARING_POSTS -> "나의 나눔 목록"
            FAVORITE_POSTS -> "나의 관심 목록"
            ACTIVITIES -> "나의 활동 내역"
            else -> ""
        }
        binding.header.btnBack.setOnClickListener {
            finish()
        }
        binding.noContentMessage.text = when (postType) {
            SHARING_POSTS -> "회원님의 나눔 게시글 목록이 없어요"
            FAVORITE_POSTS -> "회원님의 관심 목록으로 등록된 게시물이 없어요"
            ACTIVITIES -> "회원님의 활동 내역이 없어요"
            else -> ""
        }

        listAdapter = MySharingPostListAdapter()
        binding.postList.adapter = listAdapter
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.uiStateFlow.collect { (isLoading, toast, posts) ->
            toast?.let(::showToast)
            showPosts(posts)
        }
    }

    private fun showPosts(posts: List<SharingPostItem>) {
        binding.noContentView.isVisible = posts.isEmpty()
        binding.postList.isVisible = posts.isNotEmpty()
        posts.forEach {
            it.onClick = ::startSharingPostDetail
        }
        listAdapter.submitList(posts)
    }

    private fun startSharingPostDetail(postId: Int) {
        startActivity(SharingPostDetailActivity.getIntent(this, postId))
    }

    companion object {

        const val POST_TYPE = "post-type"
        const val SHARING_POSTS = 0
        const val FAVORITE_POSTS = 1
        const val ACTIVITIES = 2

        fun getIntent(context: Context, postType: Int) = Intent(context, MySharingPostListActivity::class.java).apply {
            putExtra(POST_TYPE, postType)
        }
    }
}