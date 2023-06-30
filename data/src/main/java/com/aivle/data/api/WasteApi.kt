package com.aivle.data.api

import com.aivle.data.entity.waste.WasteClassificationDocumentEntity
import com.aivle.domain.model.waste.WasteSpec
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WasteApi {

    @Multipart
    @POST("waste/image-upload")
    suspend fun imageUpload(
        @Part image: MultipartBody.Part
    ): ApiResponse<WasteClassificationDocumentEntity>

    @GET("waste/table")
    suspend fun wasteSpecTable(): ApiResponse<List<WasteSpec>>
}