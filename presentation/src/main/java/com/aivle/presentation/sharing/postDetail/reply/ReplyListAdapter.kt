package com.aivle.presentation.sharing.postDetail.reply

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.presentation.databinding.ItemReplyBinding
import com.aivle.presentation.util.common.Constants

private val DiffCallback = object : DiffUtil.ItemCallback<Reply>() {
    override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean = oldItem.reply_id == newItem.reply_id
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

    private val popupMenu: PopupMenu

    init {
        binding.root.setOnLongClickListener {
            val reply = binding.reply
            if (reply == null || reply.user.id != Constants.userId) {
                return@setOnLongClickListener false
            }
            reply.onLongClick?.invoke(reply)
            true
        }
        // 더보기 버튼 팝업 메뉴
        popupMenu = PopupMenu(binding.root.context, binding.btnTheMore).also {
            it.menu.add(Menu.NONE, 1, 1, "삭제")
            it.setOnMenuItemClickListener { menuItem ->
                val reply = binding.reply ?: return@setOnMenuItemClickListener false
                when (menuItem.itemId) {
                    1 -> reply.onClickDeleteReply?.invoke(reply)
                }
                false
            }
        }
        binding.btnTheMore.setOnClickListener {
            if (!it.isEnabled) {
                return@setOnClickListener
            }
            popupMenu.show()
        }
    }

    fun bind(reply: Reply) {
        binding.reply = reply
        binding.executePendingBindings()
        binding.btnTheMore.isEnabled = reply.user.id == Constants.userId

    }
}

private fun createReplyViewHolder(parent: ViewGroup) = ReplyViewHolder(
    ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)