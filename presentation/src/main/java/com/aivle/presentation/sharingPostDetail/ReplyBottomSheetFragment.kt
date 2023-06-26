package com.aivle.presentation.sharingPostDetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.presentation._common.repeatOnStarted
import com.aivle.presentation.databinding.BottomSheetReplyBinding
import com.aivle.presentation.sharingPostDetail.ReplyBottomSheetViewModel.Event
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyBottomSheetFragment private constructor(
    private val comment: Comment,
    private val offset: Int = 0,
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

        viewModel.loadReplies(comment.commentId)
    }

    private fun initView() {
        binding.btnClose.setOnClickListener { dismiss() }
        binding.replyListView.adapter = ReplyListAdapter().also {
            replyListAdapter = it
        }
        binding.btnInputReply.setOnClickListener {
            InputCommentBottomSheetFragment { reply ->
                val oldList = replyListAdapter.currentList
                val oldLastReply = oldList.last()
                val newList = oldList + listOf(Reply(oldLastReply.replyId + 1, oldLastReply.commentId, oldLastReply.user, reply, "방금 전"))
                replyListAdapter.submitList(newList)
            }.show(parentFragmentManager, "reply-comment-bottom-sheet")
        }
        binding.comment = comment
    }

    private fun handleEvent() = repeatOnStarted {
        viewModel.eventFlow.collect { event -> when (event) {
            is Event.None -> {}
            is Event.LoadReplies.Success -> {
                replyListAdapter.submitList(event.replies)
            }
            is Event.LoadReplies.Failure -> {
                showToast(event.message)
            }
        }}
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    class Builder(private val comment: Comment) {

        private var topOffset: Int = 0

        fun topOffset(offset: Int) = apply {
            this.topOffset = offset
        }

        fun show(fragmentManager: FragmentManager) {
            ReplyBottomSheetFragment(
                comment, topOffset
            ).show(fragmentManager, TAG)
        }
    }

    companion object {
        const val TAG = "reply-bottom-sheet"
    }
}