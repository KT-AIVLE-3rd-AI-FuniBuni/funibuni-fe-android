package com.aivle.presentation.util.model

import com.aivle.domain.model.waste.WasteSpec
import kotlin.math.roundToInt

data class ClassificationResult(
    val wasteId: Int,
    val imageTitle: String,
    val imageUrl: String,
    val groups: List<AiResultGroup>
)

data class AiResultGroup(
    val largeCategory: AiResult,
    val smallCategories: List<AiResult>,
)

data class AiResult(
    val index: Int,
    val categoryName: String,
    val probability: Float,
    val spec: WasteSpec,
) {
    val percent: Int = (probability * 100).roundToInt()
    val percentString: String = "$percent%"
    val percentStringBracket: String = "($percent%)"

    var onClick: ((AiResult) -> Unit)? = null
    var isSelected: Boolean = false
}