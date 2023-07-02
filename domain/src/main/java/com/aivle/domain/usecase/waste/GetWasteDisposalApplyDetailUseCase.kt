package com.aivle.domain.usecase.waste

import com.aivle.domain.repository.WasteRepository
import javax.inject.Inject

class GetWasteDisposalApplyDetailUseCase @Inject constructor(
    private val repository: WasteRepository
) {
    suspend operator fun invoke(wasteApplyId: Int) = repository.getWasteDisposalApplyDetail(wasteApplyId)
}