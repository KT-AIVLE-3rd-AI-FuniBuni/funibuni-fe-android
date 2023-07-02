package com.aivle.domain.repository

import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteDisposalApply
import com.aivle.domain.model.waste.WasteDisposalApplyDetail
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import kotlinx.coroutines.flow.Flow

interface WasteRepository {

    suspend fun classifyWasteImage(imageUri: String): Flow<DataResponse<WasteClassificationDocument>>

    suspend fun getWasteSpecTable(): Flow<DataResponse<List<WasteSpec>>>

    suspend fun applyWasteDisposal(apply: WasteDisposalApply): Flow<DataResponse<WasteDisposalApply>>

    suspend fun getWasteDisposalApplyDetail(wasteApplyId: Int): Flow<DataResponse<WasteDisposalApplyDetail>>

    suspend fun cancelWasteDisposalApply(wasteApplyId: Int): Flow<NothingResponse>
}