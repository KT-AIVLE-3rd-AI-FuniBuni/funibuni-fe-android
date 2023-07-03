package com.aivle.domain.usecase.myProfile

import com.aivle.domain.repository.MyBuniRepository
import javax.inject.Inject

class GetWasteDisposalApplyListUseCase @Inject constructor(
    private val repository: MyBuniRepository
) {
    suspend operator fun invoke() = repository.getWasteDisposalApplies()
}