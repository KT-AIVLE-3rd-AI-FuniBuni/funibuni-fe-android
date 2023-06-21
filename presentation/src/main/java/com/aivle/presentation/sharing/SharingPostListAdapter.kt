package com.aivle.presentation.sharing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.presentation.databinding.ItemSharingPostBinding

private val DiffCallback = object : DiffUtil.ItemCallback<SharingPostItem>() {
    override fun areItemsTheSame(oldItem: SharingPostItem, newItem: SharingPostItem): Boolean = oldItem.postId == newItem.postId
    override fun areContentsTheSame(oldItem: SharingPostItem, newItem: SharingPostItem): Boolean = oldItem == newItem
}

class SharingPostListAdapter : ListAdapter<SharingPostItem, SharingPostListItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharingPostListItemViewHolder {
        return createSharingPostListItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SharingPostListItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class SharingPostListItemViewHolder(private val binding: ItemSharingPostBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            binding.post?.let { post ->
                post.onClick!!.invoke(post.postId)
            }
        }
    }

    fun bind(postItem: SharingPostItem) {
        binding.post = postItem
        binding.executePendingBindings()
    }
}

private fun createSharingPostListItemViewHolder(parent: ViewGroup): SharingPostListItemViewHolder {
    return SharingPostListItemViewHolder(
        ItemSharingPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )
}