package com.aivle.presentation.sharing.postDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.updateLayoutParams
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.databinding.ActivitySharingPostDetailBinding
import com.aivle.presentation.sharing.postDetail.SharingPostDetailViewModel.Event
import com.aivle.presentation.util.common.KeyboardHeightProvider
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.ui.BottomUpDialog
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SharingPostDetailActivity"

@AndroidEntryPoint
class SharingPostDetailActivity : BaseActivity<ActivitySharingPostDetailBinding>(R.layout.activity_sharing_post_detail) {

    private val viewModel: SharingPostDetailViewModel by viewModels()

    private lateinit var headerAdapter: SharingPostDetailHeaderAdapter
    private lateinit var commentListAdapter: CommentListAdapter
    private lateinit var keyboardHeightProvider: KeyboardHeightProvider

    private val keyboardListener = object : KeyboardHeightProvider.OnKeyboardListener {
        override fun onHeightChanged(height: Int, isShowing: Boolean) {
            if (isShowing) {
                binding.commentContainer.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = height
                }
            } else {
                binding.commentContainer.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = 0
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        handleViewModelEvent()
        loadSharingPostDetail()

        keyboardHeightProvider = KeyboardHeightProvider(this)
        keyboardHeightProvider.addOnKeyboardListener(keyboardListener)
    }

    override fun onResume() {
        super.onResume()
        keyboardHeightProvider.onResume()
    }

    override fun onPause() {
        super.onPause()
        keyboardHeightProvider.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardHeightProvider.onDestroy()
    }

    private fun initView() {
        headerAdapter = SharingPostDetailHeaderAdapter(binding)
            .init(window, binding.appBar)
            .onBackPressed { finish() }

        binding.content.btnFavoritePost.setOnClickListener {
            val crossfade = binding.content.btnFavoritePost.crossfade
            if (crossfade == 0f) { // unlike -> like
                binding.content.btnFavoritePost.crossfade = 1f
                viewModel.likePost()
            } else { // like -> unlike
                binding.content.btnFavoritePost.crossfade = 0f
                viewModel.unlikePost()
            }
        }

        commentListAdapter = CommentListAdapter()
        binding.content.commentListView.adapter = commentListAdapter

        // 댓글 입력 버튼
        binding.btnAddComment.setOnClickListener {
            val comment = binding.edtComment.text.toString()
            if (comment.isNotBlank()) {
                viewModel.addComment(comment)
            }
            binding.edtComment.text.clear()
            keyboardHeightProvider.hideKeyboard()
        }
    }

    private fun handleViewModelEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {
            }
            is Event.Failure -> {
                showToast(event.message)
            }
            is Event.LoadPost.Success -> {
                showPostDetail(event.postDetail)
            }
            is Event.AddComment.Success -> {
                updateComments(event.comments)
            }
            is Event.DeleteComment.Success -> {
                updateComments(event.comments)
            }
        }}
    }

    private fun showPostDetail(postDetail: SharingPostDetail) {
        Log.d(TAG, "showPostDetail(): $postDetail")
        binding.postDetail = postDetail
        updateComments(postDetail.comments)
    }

    private fun showReplyBottomSheet(comment: Comment) {
        val headerHeight = binding.header.btnBack.height
        Log.d(TAG, "showReplyBottomSheet(): headerHeight=$headerHeight")

        ReplyBottomSheetFragment.Builder(comment)
            .topOffset(headerHeight)
            .show(supportFragmentManager)
    }

    private fun updateComments(comments: List<Comment>) {
        comments.onEach {
            it.onClick = ::showReplyBottomSheet
            it.onLongClick = ::showDeleteCommentConfirm
        }
        commentListAdapter.submitList(comments)
    }

    private fun loadSharingPostDetail() {
        val postId = intent.getIntExtra(EXTRA_POST_ID, -1)
        viewModel.loadSharingPostDetail(postId)
    }

    private fun showDeleteCommentConfirm(commentId: Int) {
        BottomUpDialog.Builder(this)
            .title("댓글을 삭제하시겠습니까?")
            .positiveButton {
                viewModel.deleteComment(commentId)
            }
            .show()
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