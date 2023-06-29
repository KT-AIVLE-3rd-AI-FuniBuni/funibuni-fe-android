package com.aivle.domain.model.waste

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
)