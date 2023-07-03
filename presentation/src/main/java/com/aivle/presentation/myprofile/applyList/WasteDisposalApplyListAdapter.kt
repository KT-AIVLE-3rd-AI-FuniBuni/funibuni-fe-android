package com.aivle.presentation.myprofile.applyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.presentation.databinding.ItemWasteDisposalApplyBinding
import com.aivle.presentation.util.diffcallback.WasteDisposalApplyItemDiffCallback

class WasteDisposalApplyListAdapter :
    ListAdapter<WasteDisposalApplyItem, WasteDisposalApplyItemViewHolder>(
    WasteDisposalApplyItemDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteDisposalApplyItemViewHolder {
        return createWasteDisposalApplyItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WasteDisposalApplyItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class WasteDisposalApplyItemViewHolder(
    private val binding: ItemWasteDisposalApplyBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val applyItem = binding.applyItem ?: return@setOnClickListener
            applyItem.onClick?.invoke(applyItem.waste_id)
        }
    }

    fun bind(applyItem: WasteDisposalApplyItem) {
        binding.applyItem = applyItem
    }
}

private fun createWasteDisposalApplyItemViewHolder(parent: ViewGroup) = WasteDisposalApplyItemViewHolder(
    ItemWasteDisposalApplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)