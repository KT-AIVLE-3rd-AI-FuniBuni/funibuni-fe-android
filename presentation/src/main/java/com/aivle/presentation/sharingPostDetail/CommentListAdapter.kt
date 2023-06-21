package com.aivle.presentation.sharingPostDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.presentation.databinding.ItemCommentBinding

private val DiffCallback = object : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem.commentId == newItem.commentId
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

    init {
        itemView.setOnClickListener {
            binding.comment?.let { it.onClick?.invoke(it) }
        }
    }

    fun bind(comment: Comment) {
        binding.comment = comment
        binding.executePendingBindings()
    }
}

private fun createCommentViewHolder(parent: ViewGroup) = CommentViewHolder(
    ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)