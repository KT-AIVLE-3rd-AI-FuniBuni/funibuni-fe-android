package com.aivle.presentation.disposal.wasteclassification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.waste.LargeCategoryResult
import com.aivle.presentation._common.CategoryImageBinder
import com.aivle.presentation.databinding.ItemResultLargeCategoryBinding
import kotlin.math.roundToInt

class LargeCategoryResultListAdapter :
    ListAdapter<LargeCategoryResult, LargeCategoryResultViewHolder>(object : DiffUtil.ItemCallback<LargeCategoryResult>() {
        override fun areItemsTheSame(oldItem: LargeCategoryResult, newItem: LargeCategoryResult): Boolean = oldItem.index_large_category == newItem.index_large_category
        override fun areContentsTheSame(oldItem: LargeCategoryResult, newItem: LargeCategoryResult): Boolean = oldItem.index_large_category == newItem.index_large_category
    })
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LargeCategoryResultViewHolder {
        return createLargeCategoryResultViewHolder(parent)
    }

    override fun onBindViewHolder(holder: LargeCategoryResultViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }
}

class LargeCategoryResultViewHolder(
    private val binding: ItemResultLargeCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var result: LargeCategoryResult? = null

    init {
        binding.root.setOnClickListener {
            result?.onClick?.invoke(result!!.index_large_category)
        }
    }

    fun bind(result: LargeCategoryResult, position: Int) {
        this.result = result

        val icon = if (position == 0) {
            CategoryImageBinder.circle(result.index_large_category)
        } else {
            CategoryImageBinder.circleGray(result.index_large_category)
        }

        binding.icon.setImageResource(icon)
        binding.largeCategoryName.text = result.large_category_simple_name
        binding.largeCategoryProbability.text = "${(result.probability * 100).roundToInt()}%"
    }
}

private fun createLargeCategoryResultViewHolder(parent: ViewGroup) = LargeCategoryResultViewHolder(
    ItemResultLargeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
)