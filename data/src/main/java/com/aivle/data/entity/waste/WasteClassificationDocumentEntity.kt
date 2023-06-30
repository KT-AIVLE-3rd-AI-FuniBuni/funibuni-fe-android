package com.aivle.data.entity.waste

data class WasteClassificationDocumentEntity(
    val image_title: String,
    val image_url: String,
    val labels: List<WasteClassificationRankEntity>,
    val first_large_category_name: String,
    val waste_id: Int,
    val user: Int,
    val first_large_category_waste_specs: List<WasteSpecEntity>,
    val all_waste_specs: List<WasteSpecEntity>,
)
