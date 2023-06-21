package com.aivle.presentation.sharingPostDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.presentation.databinding.ItemReplyBinding

private val DiffCallback = object : DiffUtil.ItemCallback<Reply>() {
    override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean = oldItem.replyId == newItem.replyId
    override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean = oldItem == newItem
}

class ReplyListAdapter : ListAdapter<Reply, ReplyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        return createReplyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class ReplyViewHolder(private val binding: ItemReplyBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(reply: Reply) {
        binding.reply = reply
        binding.executePendingBindings()
    }
}

private fun createReplyViewHolder(parent: ViewGroup) = ReplyViewHolder(
    ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)