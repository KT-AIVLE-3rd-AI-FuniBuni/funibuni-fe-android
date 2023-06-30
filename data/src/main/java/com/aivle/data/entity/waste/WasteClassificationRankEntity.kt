package com.aivle.data.entity.waste

import com.google.gson.annotations.SerializedName

data class WasteClassificationRankEntity(
    @SerializedName("large-category") val large_category: LargeCategoryResultEntity,
    @SerializedName("small-category") val small_category: SmallCategoryResultEntity,
)