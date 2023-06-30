package com.aivle.domain.model.waste

data class WasteClassificationRank(
    val large_category: LargeCategoryResult,
    val small_category: SmallCategoryResult,
)