package com.aivle.domain.model.waste

import java.text.DecimalFormat

data class WasteSpec(
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
    val feeString: String get() = DecimalFormat("#,###").format(fee) + "원"

    var onClick1: ((WasteSpec) -> Unit)? = null
    var onClick2: ((WasteSpec) -> Unit)? = null
}