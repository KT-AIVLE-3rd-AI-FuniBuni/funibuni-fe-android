package com.aivle.domain.repository

import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow

interface WasteRepository {

    suspend fun classifyWasteImage(imageUri: String): Flow<DataResponse<WasteClassificationDocument>>
}