package com.aivle.presentation.myprofile.postList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.presentation.databinding.ItemSharingPostBinding
import com.aivle.presentation.util.diffcallback.SharingPostItemDiffCallback

class MySharingPostListAdapter : ListAdapter<SharingPostItem, SharingPostItemViewHolder>(SharingPostItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharingPostItemViewHolder {
        return createSharingPostItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SharingPostItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class SharingPostItemViewHolder(private val binding: ItemSharingPostBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val postItem = binding.postItem ?: return@setOnClickListener
            postItem.onClick?.invoke(postItem.post_id)
        }
    }

    fun bind(postItem: SharingPostItem) {
        binding.postItem = postItem
    }
}

private fun createSharingPostItemViewHolder(parent: ViewGroup) = SharingPostItemViewHolder(
    ItemSharingPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)