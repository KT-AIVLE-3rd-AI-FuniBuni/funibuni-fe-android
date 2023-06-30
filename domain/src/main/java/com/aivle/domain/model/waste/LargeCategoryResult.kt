package com.aivle.domain.model.waste

data class LargeCategoryResult(
    val index_large_category: Int,
    val large_category_name: String,
    val probability: Float,
) {
    val large_category_simple_name: String = large_category_name.split("(").first()

    var onClick: ((index_large_category: Int) -> Unit)? = null
}