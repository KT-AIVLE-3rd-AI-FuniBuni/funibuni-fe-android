package com.aivle.presentation.sharing.postDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseActivity
import com.aivle.presentation.databinding.ActivitySharingPostDetailBinding
import com.aivle.presentation.sharing.postDetail.SharingPostDetailViewModel.Event
import com.aivle.presentation.sharing.postDetail.reply.ReplyBottomSheetFragment
import com.aivle.presentation.sharing.postEdit.EditSharingPostActivity
import com.aivle.presentation.util.common.Constants
import com.aivle.presentation.util.common.KeyboardHeightProvider
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.customView.BottomUpDialog
import com.loggi.core_util.extensions.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingPostDetailActivity : BaseActivity<ActivitySharingPostDetailBinding>(R.layout.activity_sharing_post_detail) {

    private val viewModel: SharingPostDetailViewModel by viewModels()

    private lateinit var headerAdapter: SharingPostDetailHeaderAdapter
    private lateinit var commentListAdapter: CommentListAdapter
    private lateinit var keyboardHeightProvider: KeyboardHeightProvider

    private var rootLayoutBottomMarginOffset = -1 // 하단 시스템 네비게이션바 높이

    private val postId: Int?
        get() = intent.getIntExtra(EXTRA_POST_ID, -1).takeIf { it != -1 }

    private val keyboardListener = object : KeyboardHeightProvider.OnKeyboardListener {
        override fun onHeightChanged(height: Int, isShowing: Boolean) {
            // 하단 시스템 네비게이션바 높이
            if (rootLayoutBottomMarginOffset == -1) {
                rootLayoutBottomMarginOffset = binding.rootLayout.marginBottom
            }
            if (isShowing) {
                binding.rootLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = height + rootLayoutBottomMarginOffset
                }
            } else {
                binding.rootLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = rootLayoutBottomMarginOffset
                }
            }
        }
    }

    private val updatePostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            postId?.let { id ->
                viewModel.loadSharingPostDetail(id)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
//        binding.rootLayout.isFocusableInTouchMode = true
//        binding.rootLayout.isClickable = true
//        binding.rootLayout.setOnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                binding.edtComment.clearFocus()
//                hideSoftInput(binding.edtComment)
//            }
//            false
//        }

        headerAdapter = SharingPostDetailHeaderAdapter(binding)
            .init(window, binding.appBar)
            .onBackPressed { finish() }

        binding.header.btnTheMore.setOnClickListener {
            showTheMoreMenu()
        }

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

    private fun showTheMoreMenu() {
        val popupMenu = PopupMenu(this, binding.header.btnTheMore)
        menuInflater.inflate(R.menu.menu_post_detail_the_more, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit_post -> {
                    postId?.let { id ->
                        updatePostLauncher.launch(EditSharingPostActivity.getIntent(this, id))
                    }
                }
                R.id.menu_delete_post -> {
                    viewModel.deletePost()
                }
                R.id.menu_complete_sharing -> {
                    viewModel.completeSharingPost()
                }
            }
            false
        }
        popupMenu.show()
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
            is Event.CompleteSharingPost.Success -> {
                setResult(RESULT_OK)
                finish()
            }
            is Event.DeletePost.Success -> {
                showToast("게시물이 삭제되었습니다")
                setResult(RESULT_OK)
                finish()
            }
        }}
    }

    private fun loadSharingPostDetail() {
        val postId = intent.getIntExtra(EXTRA_POST_ID, -1)
        viewModel.loadSharingPostDetail(postId)
    }

    private fun showPostDetail(postDetail: SharingPostDetail) {
        log("showPostDetail(): $postDetail")
        binding.postDetail = postDetail
        updateComments(postDetail.comments)

        // 자신의 글이 아니면 더보기(수정,삭제 등) 버튼 숨기기
        if (postDetail.user.id != Constants.userId) {
            binding.header.btnTheMore.isVisible = false
        }
    }

    private fun updateComments(comments: List<Comment>) {
        comments.onEach {
            it.onClick = ::showReplyBottomSheet
            it.onLongClick = ::showDeleteCommentConfirm
            it.onClickEditComment = ::showEditTextForEditComment
            it.onClickDeleteComment = ::showDeleteCommentConfirm
        }
        commentListAdapter.submitList(comments)
    }

    private fun showReplyBottomSheet(comment: Comment) {
        val headerHeight = binding.header.btnBack.height
        log("showReplyBottomSheet(): headerHeight=$headerHeight")

        ReplyBottomSheetFragment.Builder(comment)
            .topOffset(headerHeight)
            .replyCountChangedListener { isChanged ->
                if (isChanged) {
                    postId?.let { id ->
                        viewModel.loadSharingPostDetail(id)
                    }
                }
            }
            .show(supportFragmentManager)
    }

    private fun showDeleteCommentConfirm(comment: Comment) {
        BottomUpDialog.Builder(this)
            .title("댓글을 삭제하시겠습니까?")
            .positiveButton {
                viewModel.deleteComment(comment.comment_id)
            }
            .show()
    }

    private fun showEditTextForEditComment(comment: Comment) {
        // TODO
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