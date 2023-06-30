package com.aivle.presentation.disposal.wasteclassification.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.databinding.ItemSmallCategoryBinding

private val DiffCallback = object : DiffUtil.ItemCallback<WasteSpec>() {
    override fun areItemsTheSame(oldItem: WasteSpec, newItem: WasteSpec): Boolean = oldItem.waste_spec_id == newItem.waste_spec_id
    override fun areContentsTheSame(oldItem: WasteSpec, newItem: WasteSpec): Boolean = oldItem.waste_spec_id == newItem.waste_spec_id
}

class WasteCategorySmallListAdapter : ListAdapter<WasteSpec, SmallCategoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallCategoryViewHolder {
        return createSmallCategoryViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SmallCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class SmallCategoryViewHolder(private val binding: ItemSmallCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    private var wasteSpec: WasteSpec? = null

    init {
        binding.root.setOnClickListener {
            val spec = wasteSpec ?: return@setOnClickListener
            spec.onClick2?.invoke(spec)
        }
    }

    fun bind(wasteSpec: WasteSpec) {
        this.wasteSpec = wasteSpec
        binding.smallCategoryName.text = wasteSpec.small_category
    }
}

private fun createSmallCategoryViewHolder(parent: ViewGroup) = SmallCategoryViewHolder(
    ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)