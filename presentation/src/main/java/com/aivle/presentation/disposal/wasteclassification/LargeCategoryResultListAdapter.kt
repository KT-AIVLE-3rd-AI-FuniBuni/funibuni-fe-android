package com.aivle.presentation.disposal.wasteclassification

import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aivle.domain.model.waste.LargeCategoryResult
import com.aivle.presentation.util.common.CategoryImageBinder
import com.aivle.presentation.databinding.ItemResultLargeCategoryBinding
import com.aivle.presentation.util.model.AiResult
import kotlin.math.roundToInt

private const val TAG = "LargeCategoryResultListAdapter"
private const val CHANGED_SELECTION = "changed-selection"
private val DiffCallback = object : DiffUtil.ItemCallback<AiResult>() {
    override fun areItemsTheSame(oldItem: AiResult, newItem: AiResult): Boolean = oldItem.index == newItem.index
    override fun areContentsTheSame(oldItem: AiResult, newItem: AiResult): Boolean = oldItem.index == newItem.index
}

class LargeCategoryResultListAdapter
    : ListAdapter<AiResult, LargeCategoryResultViewHolder>(DiffCallback) {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LargeCategoryResultViewHolder {
        return createLargeCategoryResultViewHolder(parent, ::updateSelectionItems)
    }

    override fun onBindViewHolder(
        holder: LargeCategoryResultViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else if (payloads.any { it == CHANGED_SELECTION } && position != selectedPosition) {
            holder.cancelSelection(currentList[position].index)
        }
    }

    override fun onBindViewHolder(holder: LargeCategoryResultViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun submitList(list: List<AiResult>?) {
        // 첫 번쨰 아이템을 기본 선택 값으로 설정
        selectedPosition = 0
        list?.forEachIndexed { index, result ->
            result.isSelected = (index == 0)
        }
        super.submitList(list)
    }

    private fun updateSelectionItems(newSelectedPosition: Int) {
        selectedPosition = newSelectedPosition
        currentList.forEachIndexed { index, item -> item.isSelected = index == newSelectedPosition }
        notifyItemRangeChanged(0, itemCount, CHANGED_SELECTION)
    }
}

class LargeCategoryResultViewHolder(
    private val binding: ItemResultLargeCategoryBinding,
    private val updateSelectionItem: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener { _ ->
            val result = binding.result ?: return@setOnClickListener
            result.onClick?.invoke(result)

            if (!result.isSelected) {
                result.isSelected = true
                updateSelectionIcon(true, result.index)
                updateSelectionItem(adapterPosition)
            }
        }
    }

    fun bind(result: AiResult) {
        binding.result = result

        updateSelectionIcon(result.isSelected, result.index)

        val iconMedal = CategoryImageBinder.medal(adapterPosition)
        binding.iconMedal.setImageResource(iconMedal)
    }

    fun cancelSelection(indexLargeCategory: Int) {
        updateSelectionIcon(false, indexLargeCategory)
    }

    private fun updateSelectionIcon(isSelected: Boolean, indexLargeCategory: Int) {
        val icon = if (isSelected) {
            CategoryImageBinder.circle(indexLargeCategory)
        } else {
            CategoryImageBinder.circleGray(indexLargeCategory)
        }
        binding.icon.setImageResource(icon)
        binding.largeCategoryName.setTextColor(if (isSelected) Color.BLUE else Color.DKGRAY)
        binding.largeCategoryName.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        binding.largeCategoryProbability.setTextColor(if (isSelected) Color.BLUE else Color.DKGRAY)
        binding.largeCategoryProbability.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.interactiveContainer.scaleUpForced()
        }, 50)
    }
}

private fun createLargeCategoryResultViewHolder(
    parent: ViewGroup,
    updateSelectionItem: (position: Int) -> Unit,
) = LargeCategoryResultViewHolder(
    ItemResultLargeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    updateSelectionItem,
)