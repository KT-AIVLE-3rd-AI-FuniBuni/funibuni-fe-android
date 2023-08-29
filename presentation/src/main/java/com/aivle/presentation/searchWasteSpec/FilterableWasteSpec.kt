package com.aivle.presentation.searchWasteSpec

import android.text.SpannableString
import androidx.core.text.clearSpans
import androidx.recyclerview.widget.DiffUtil
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.presentation.util.search.HangulJamoTextMatcher

data class FilterableWasteSpec(
    val wasteSpec: WasteSpec
) : HangulJamoTextMatcher.TextItem {

    var _onLongClick: ((WasteSpec) -> Unit)? = null

    private var _spannableString = SpannableString(wasteSpec.large_category)
    private var _matchingPoint: Long = 0

    override val comparator: String
        get() = wasteSpec.large_category

    override val spannableString: SpannableString
        get() = _spannableString

    override var matchingPoint: Long
        get() = _matchingPoint
        set(value) { _matchingPoint = value }

    fun resetPoint() {
        _matchingPoint = 0
        _spannableString.clearSpans()
    }

    fun clone() = FilterableWasteSpec(wasteSpec).also {
        it._spannableString = _spannableString
        it._matchingPoint = _matchingPoint
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<FilterableWasteSpec>() {
            override fun areItemsTheSame(oldItem: FilterableWasteSpec, newItem: FilterableWasteSpec): Boolean =
                oldItem.wasteSpec.waste_spec_id == newItem.wasteSpec.waste_spec_id
            override fun areContentsTheSame(oldItem: FilterableWasteSpec, newItem: FilterableWasteSpec): Boolean =
                oldItem.wasteSpec == newItem.wasteSpec && oldItem.matchingPoint == newItem.matchingPoint
        }
    }
}