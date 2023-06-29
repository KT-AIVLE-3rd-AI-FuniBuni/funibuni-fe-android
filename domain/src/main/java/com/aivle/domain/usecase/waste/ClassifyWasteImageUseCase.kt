package com.aivle.domain.usecase.waste

import com.aivle.domain.repository.WasteRepository
import javax.inject.Inject

class ClassifyWasteImageUseCase @Inject constructor(
    private val repository: WasteRepository
) {
    suspend operator fun invoke(imageUri: String) = repository.classifyWasteImage(imageUri)
}