package com.aivle.presentation.disposal.wasteclassification

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aivle.presentation.R
import com.aivle.presentation.databinding.ItemResultSmallCategoryBinding
import com.aivle.presentation.util.model.AiResult

private const val CHANGED_SELECTION = "changed-selection"
private val DiffCallback = object : DiffUtil.ItemCallback<AiResult>() {
    override fun areItemsTheSame(oldItem: AiResult, newItem: AiResult): Boolean = oldItem === newItem
    override fun areContentsTheSame(oldItem: AiResult, newItem: AiResult): Boolean = oldItem == newItem
}

class SmallCategoryResultListAdapter : ListAdapter<AiResult, SmallCategoryResultViewHolder>(DiffCallback) {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallCategoryResultViewHolder {
        return createItemViewHolder(parent, ::onChangedSelectionItem)
    }

    override fun onBindViewHolder(holder: SmallCategoryResultViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else if (payloads.any { it == CHANGED_SELECTION } && position != selectedPosition) {
            holder.cancelSelection()
        }
    }

    override fun onBindViewHolder(holder: SmallCategoryResultViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private fun onChangedSelectionItem(position: Int) {
        selectedPosition = position
        currentList.forEachIndexed { index, item -> item.isSelected = (index == position) }
        notifyItemRangeChanged(0, itemCount, CHANGED_SELECTION)
    }
}

class SmallCategoryResultViewHolder(
    private val binding: ItemResultSmallCategoryBinding,
    private val onChangedSelectionItem: (position: Int) -> Unit,
) : ViewHolder(binding.root) {

    private val selectedBackground = AppCompatResources.getDrawable(binding.root.context, R.drawable.bg_item_small_cat_selected)

    init {
        binding.root.setOnClickListener { _ ->
            val result = binding.result ?: return@setOnClickListener
            result.onClick?.invoke(result)

            if (!result.isSelected) {
                result.isSelected = true
                updateSelectionView(true)
                onChangedSelectionItem(adapterPosition)
            }
        }
    }

    fun bind(result: AiResult) {
        binding.result = result
        updateSelectionView(result.isSelected)
    }

    fun cancelSelection() {
        updateSelectionView(false)
    }

    private fun updateSelectionView(isSelected: Boolean) {
        binding.background.background = if (isSelected) selectedBackground else null
        binding.smallCategoryName.setTextColor(if (isSelected) Color.BLUE else Color.BLACK)
        binding.smallCategoryProbability.setTextColor(if (isSelected) Color.BLUE else Color.BLACK)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.interactiveContainer.scaleUpForced()
        }, 50)
    }
}

private fun createItemViewHolder(
    parent: ViewGroup,
    onChangedSelectionItem: (position: Int) -> Unit,
) = SmallCategoryResultViewHolder(
    ItemResultSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    onChangedSelectionItem,
)