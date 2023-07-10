package com.aivle.presentation.sharing.postDetail

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.presentation.R
import com.aivle.presentation.databinding.ItemCommentBinding
import com.aivle.presentation.util.common.Constants

private val DiffCallback = object : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem.comment_id == newItem.comment_id
    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem == newItem
}

class CommentListAdapter : ListAdapter<Comment, CommentViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return createCommentViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

    private var isTheMoreEnabled = false
    private val popupMenu: PopupMenu

    init {
        // 클릭 이벤트: 대댓글 보기
        binding.container.setOnClickListener {
            binding.comment?.let { it.onClick?.invoke(it) }
        }
        // 롱클릭 이벤트: 삭제
        binding.container.setOnLongClickListener {
            if (!isTheMoreEnabled) return@setOnLongClickListener false
            val comment = binding.comment ?: return@setOnLongClickListener false
            comment.onLongClick?.invoke(comment)
            true
        }
        // 더보기 버튼 팝업 메뉴
        popupMenu = PopupMenu(binding.root.context, binding.btnTheMore).also {
            // it.menu.add(Menu.NONE, 1, 1, "수정")
            it.menu.add(Menu.NONE, 1, 1, "삭제")
            it.setOnMenuItemClickListener { menuItem ->
                val comment = binding.comment ?: return@setOnMenuItemClickListener false
                when (menuItem.itemId) {
                    // 1 -> comment.onClickEditComment?.invoke(comment)
                    1 -> comment.onClickDeleteComment?.invoke(comment)
                }
                false
            }
        }
        // 더보기 버튼 리스너
        binding.btnTheMore.setOnClickListener {
            if (isTheMoreEnabled) {
                popupMenu.show()
            }
        }
    }

    fun bind(comment: Comment) {
        binding.comment = comment
        binding.executePendingBindings()
        isTheMoreEnabled = comment.user.id == Constants.userId
        binding.btnTheMore.isEnabled = isTheMoreEnabled
    }
}

private fun createCommentViewHolder(parent: ViewGroup) = CommentViewHolder(
    ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)