package com.aivle.domain.model.waste

data class SmallCategoryResult(
    val index_small_category: Int,
    val small_category_name: String,
    val probability: Float,
) {
    var onClick: ((index_small_category: Int) -> Unit)? = null
}