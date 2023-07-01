package com.aivle.presentation.disposal.wasteclassification.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.util.common.CategoryImageBinder
import com.aivle.presentation.databinding.ItemWasteCategoryBinding
import com.aivle.presentation.databinding.ItemWasteCategoryGroupBinding

private const val TAG = "WasteCategoryListAdapter"

private val DiffCallback = object : DiffUtil.ItemCallback<WasteSpec>() {
    override fun areItemsTheSame(oldItem: WasteSpec, newItem: WasteSpec): Boolean = oldItem.waste_spec_id == newItem.waste_spec_id
    override fun areContentsTheSame(oldItem: WasteSpec, newItem: WasteSpec): Boolean = oldItem.waste_spec_id == newItem.waste_spec_id
}

/**
 * Parent ListAdapter
 * */
class WasteCategoryListAdapter(
    private val allWasteSpecs: List<WasteSpec>
) : ListAdapter<WasteSpec, WasteCategoryGroupViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteCategoryGroupViewHolder {
        return createWasteCategoryGroupViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WasteCategoryGroupViewHolder, position: Int) {
        val largeCategoryGroup = allWasteSpecs.filter { it.top_category == currentList[position].top_category }
            .distinctBy { it.index_large_category }

        holder.bind(largeCategoryGroup)
    }
}

class WasteCategoryGroupViewHolder(private val binding: ItemWasteCategoryGroupBinding) : RecyclerView.ViewHolder(binding.root) {

    private val childAdapter = WasteCategoryChildListAdapter()

    init {
        binding.childListView.layoutManager = GridLayoutManager(itemView.context, 3)
        binding.childListView.apply {
            layoutManager = GridLayoutManager(itemView.context, 3)
            adapter = childAdapter
        }
    }

    fun bind(largeCategoryGroup: List<WasteSpec>) {
        binding.categoryGroupName.text = if (largeCategoryGroup.isEmpty()) {
            ""
        } else {
            largeCategoryGroup.first().top_category
        }
        childAdapter.submitList(largeCategoryGroup)
    }
}

private fun createWasteCategoryGroupViewHolder(parent: ViewGroup) = WasteCategoryGroupViewHolder(
    ItemWasteCategoryGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)

/**
 * Child ListAdapter
 * */
class WasteCategoryChildListAdapter : ListAdapter<WasteSpec, WasteCategoryItemViewHolder>(
    DiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteCategoryItemViewHolder {
        return createWasteCategoryItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WasteCategoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class WasteCategoryItemViewHolder(private val binding: ItemWasteCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    private var wasteSpec: WasteSpec? = null

    init {
        binding.root.setOnClickListener {
            val wasteSpec = this.wasteSpec ?: return@setOnClickListener
            wasteSpec.onClick1?.invoke(wasteSpec)
        }
    }

    fun bind(wasteSpec: WasteSpec) {
        this.wasteSpec = wasteSpec

        val wasteCategoryIcon = CategoryImageBinder.normal(wasteSpec.index_large_category)
        binding.icon.setImageResource(wasteCategoryIcon)

        val subNameIndex = wasteSpec.large_category.indexOf("(")
        if (subNameIndex != -1) {
            val largeCategoryName = wasteSpec.large_category.take(subNameIndex)
            val largeCategoryNameSub = wasteSpec.large_category.drop(subNameIndex)
            binding.largeCategoryName.text = largeCategoryName
            binding.largeCategoryNameSub.text = largeCategoryNameSub
        } else {
            binding.largeCategoryName.text = wasteSpec.large_category
            binding.largeCategoryNameSub.text = ""
        }
    }
}

private fun createWasteCategoryItemViewHolder(parent: ViewGroup) = WasteCategoryItemViewHolder(
    ItemWasteCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)