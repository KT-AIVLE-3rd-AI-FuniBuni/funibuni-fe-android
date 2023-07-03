package com.aivle.presentation.util.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.model.waste.WasteDisposalApplyItem

val SharingPostItemDiffCallback = object : DiffUtil.ItemCallback<SharingPostItem>() {
    override fun areItemsTheSame(oldItem: SharingPostItem, newItem: SharingPostItem): Boolean {
        return oldItem.post_id == newItem.post_id
    }

    override fun areContentsTheSame(oldItem: SharingPostItem, newItem: SharingPostItem): Boolean {
        return oldItem == newItem
    }
}

val WasteDisposalApplyItemDiffCallback = object : DiffUtil.ItemCallback<WasteDisposalApplyItem>() {
    override fun areItemsTheSame(oldItem: WasteDisposalApplyItem, newItem: WasteDisposalApplyItem): Boolean {
        return oldItem.waste_id == newItem.waste_id
    }

    override fun areContentsTheSame(oldItem: WasteDisposalApplyItem, newItem: WasteDisposalApplyItem): Boolean {
        return oldItem == newItem
    }
}