package com.aivle.presentation.sharing.postDetail.reply

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.presentation.databinding.BottomSheetReplyBinding
import com.aivle.presentation.sharing.postDetail.reply.ReplyBottomSheetViewModel.Event
import com.aivle.presentation.util.ext.repeatOnStarted
import com.aivle.presentation.util.ext.showToast
import com.aivle.presentation_design.interactive.customView.BottomUpDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loggi.core_util.extensions.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyBottomSheetFragment private constructor(
    private val comment: Comment,
    private val offset: Int = 0,
    private val onReplyCountChanged: ((isChanged: Boolean) -> Unit)? = null,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetReplyBinding
    private lateinit var replyListAdapter: ReplyListAdapter
    private val viewModel: ReplyBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    @SuppressLint("RestrictedApi", "VisibleForTests")
    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(offset)
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isDraggable = false
        behavior.disableShapeAnimations()
    }

    private fun getBottomSheetDialogDefaultHeight(offset: Int): Int {
        return getWindowHeight() - offset
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleEvent()

        viewModel.loadReplies(comment.post_id, comment.comment_id)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val isReplyCountChanged = comment.reply_count != replyListAdapter.itemCount
        onReplyCountChanged?.invoke(isReplyCountChanged)
    }

    private fun initView() {
        binding.btnClose.setOnClickListener { dismiss() }

        binding.swipeRefresh.setOnRefreshListener {
            if (binding.swipeRefresh.isRefreshing) {
                viewModel.loadReplies(comment.post_id, comment.comment_id)
            }
        }

        binding.replyListView.adapter = ReplyListAdapter().also {
            replyListAdapter = it
        }
        binding.btnInputReply.setOnClickListener {
            InputCommentBottomSheetFragment { reply ->
                viewModel.addReply(comment.post_id, comment.comment_id, reply)
            }.show(parentFragmentManager, "reply-comment-bottom-sheet")
        }
        binding.comment = comment
        binding.commentLayout.btnTheMore.visibility = View.INVISIBLE
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {}
            is Event.LoadReplies.Success -> {
                log("Event.LoadReplies.Success: ${event.replies.count()}")
                updateReplies(event.replies)
            }
            is Event.AddReply.Success -> {
                updateReplies(event.replies)
            }
            is Event.DeleteReply.Success -> {
                updateReplies(event.replies)
            }
            is Event.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun updateReplies(replies: List<Reply>) {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
        }
        replies.forEach {
            it.onLongClick = ::showDeleteConfirmDialog
            it.onClickDeleteReply = ::showDeleteConfirmDialog
        }
        replyListAdapter.submitList(replies)
    }

    private fun showDeleteConfirmDialog(reply: Reply) {
        BottomUpDialog.Builder(requireActivity())
            .title("댓글을 삭제하시겠습니까?")
            .positiveButton {
                viewModel.deleteReply(comment.post_id, comment.comment_id, reply.reply_id)
            }
            .show()
    }

    class Builder(private val comment: Comment) {

        private var topOffset: Int = 0
        private var listener: ((isChanged: Boolean) -> Unit)? = null

        fun topOffset(offset: Int) = apply {
            this.topOffset = offset
        }

        fun replyCountChangedListener(listener: (isChanged: Boolean) -> Unit) = apply {
            this.listener = listener
        }

        fun show(fragmentManager: FragmentManager) {
            ReplyBottomSheetFragment(
                comment, topOffset, listener
            ).show(fragmentManager, "tag")
        }
    }
}