package com.aivle.domain.model.waste

data class WasteClassificationDocument(
    val image_title: String,
    val image_url: String,
    val first_large_category_name: String,
    val waste_id: Int,
    val user: Int,
    val first_large_category_waste_specs: List<WasteSpec>,
    val all_waste_specs: List<WasteSpec>,
)