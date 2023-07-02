package com.aivle.domain.usecase.waste

import com.aivle.domain.model.waste.WasteDisposalApply
import com.aivle.domain.repository.WasteRepository
import javax.inject.Inject

class ApplyWasteDisposalUseCase @Inject constructor(
    private val repository: WasteRepository
) {
    suspend operator fun invoke(apply: WasteDisposalApply) = repository.applyWasteDisposal(apply)
}