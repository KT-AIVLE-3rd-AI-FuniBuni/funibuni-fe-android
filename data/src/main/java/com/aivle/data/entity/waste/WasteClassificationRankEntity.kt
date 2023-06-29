package com.aivle.data.entity.waste

data class WasteClassificationRankEntity(
    val large_category: List<LargeCategoryResultEntity>,
    val small_category: List<SmallCategoryResultEntity>,
)