package com.aivle.presentation.disposal.wasteclassification

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.aivle.domain.model.waste.WasteSpec

data class WasteSpecWrapper(
    val waste_spec_id: Int,
    val index_large_category: Int,
    val index_small_category: Int,
    val city: String,
    val district: String,
    val top_category: String,
    val large_category: String,
    val small_category: String,
    val size_range: String, // "nan" is null
    val is_exists_small_cat_model: Boolean,
    val type: String,
    val fee: Int,
) {
    var onClick1: ((ImageView, WasteSpec) -> Unit)? = null
    var onClick2: ((WasteSpec) -> Unit)? = null

    companion object {

        val DiffCallback = object : DiffUtil.ItemCallback<WasteSpecWrapper>() {
            override fun areItemsTheSame(oldItem: WasteSpecWrapper, newItem: WasteSpecWrapper): Boolean = oldItem.waste_spec_id == newItem.waste_spec_id
            override fun areContentsTheSame(oldItem: WasteSpecWrapper, newItem: WasteSpecWrapper): Boolean = oldItem.waste_spec_id == newItem.waste_spec_id
        }
    }
}

fun WasteSpecWrapper.toOrigin() = WasteSpec(waste_spec_id, index_large_category, index_small_category, city, district, top_category, large_category, small_category, size_range, is_exists_small_cat_model, type, fee)
fun WasteSpec.toWrapper() = WasteSpecWrapper(waste_spec_id, index_large_category, index_small_category, city, district, top_category, large_category, small_category, size_range, is_exists_small_cat_model, type, fee)