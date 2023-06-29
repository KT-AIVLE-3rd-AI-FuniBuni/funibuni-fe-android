package com.aivle.data.repository

import android.util.Log
import com.aivle.data.api.WasteApi
import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.data.mapper.toModel
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.repository.WasteRepository
import com.aivle.domain.response.DataResponse
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "WasteRepositoryImpl"

class WasteRepositoryImpl @Inject constructor(
    @FuniBuniApiQualifier private val api: WasteApi
) : WasteRepository {

    override suspend fun classifyWasteImage(imageUri: String): Flow<DataResponse<WasteClassificationDocument>> = flow {
        Log.d(TAG, "classifyWasteImage(): $imageUri")
        val imageFile = File(imageUri)
        val requestFile = RequestBody.create("image/*".toMediaType(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        Log.d(TAG, "classifyWasteImage(): $imageFile")

        api.imageUpload(imagePart)
            .suspendOnSuccess {
                val response = DataResponse.Success(data.toModel())
                emit(response)
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }
}