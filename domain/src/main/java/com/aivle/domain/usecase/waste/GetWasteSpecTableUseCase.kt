package com.aivle.domain.usecase.waste

import com.aivle.domain.repository.WasteRepository
import javax.inject.Inject

class GetWasteSpecTableUseCase @Inject constructor(
    private val repository: WasteRepository
) {
    suspend operator fun invoke() = repository.getWasteSpecTable()
}