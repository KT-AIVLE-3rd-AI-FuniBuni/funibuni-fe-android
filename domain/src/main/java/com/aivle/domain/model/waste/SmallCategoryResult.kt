package com.aivle.domain.model.waste

import kotlin.math.roundToInt

data class SmallCategoryResult(
    val index_small_category: Int,
    val small_category_name: String,
    val probability: Float,
) {
    val probabilityInt = probability.toInt()
    val probabilityString = "(${(probability * 100).roundToInt()}%)"
    var onClick: ((index_small_category: Int) -> Unit)? = null
    var isSelected: Boolean = false
}