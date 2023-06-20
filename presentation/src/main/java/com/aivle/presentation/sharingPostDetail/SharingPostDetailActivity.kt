package com.aivle.presentation.sharingPostDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivitySharingPostDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingPostDetailActivity : BaseActivity<ActivitySharingPostDetailBinding>(R.layout.activity_sharing_post_detail) {

    private val viewModel: SharingPostDetailViewModel by viewModels()

    private lateinit var headerAdapter: SharingPostDetailHeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        headerAdapter = SharingPostDetailHeaderAdapter(binding.header)
            .init(window, binding.appBar)
            .onBackPressed { finish() }
    }

    companion object {

        private const val EXTRA_POST_ID = "post_id"

        fun getIntent(context: Context, postId: Int): Intent {
            return Intent(context, SharingPostDetailActivity::class.java).apply {
                putExtra(EXTRA_POST_ID, postId)
            }
        }
    }
}