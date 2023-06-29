package com.aivle.domain.model.waste

data class WasteClassificationRank(
    val large_category: List<LargeCategoryResult>,
    val small_category: List<SmallCategoryResult>,
)