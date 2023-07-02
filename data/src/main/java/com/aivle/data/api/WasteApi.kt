package com.aivle.data.api

import com.aivle.data.entity.waste.WasteClassificationDocumentEntity
import com.aivle.data.entity.waste.WasteDisposalApplyDetailEntity
import com.aivle.data.entity.waste.WasteDisposalApplyEntity
import com.aivle.domain.model.waste.WasteSpec
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface WasteApi {

    @Multipart
    @POST("waste/image-upload")
    suspend fun imageUpload(
        @Part image: MultipartBody.Part
    ): ApiResponse<WasteClassificationDocumentEntity>

    @POST("waste/table")
    suspend fun wasteSpecTable(): ApiResponse<List<WasteSpec>>

    @PATCH("waste/apply")
    suspend fun applyWasteDisposal(
        @Body apply: WasteDisposalApplyEntity
    ): ApiResponse<WasteDisposalApplyEntity>

    @DELETE("waste/apply/{waste_id}")
    suspend fun cancelWasteDisposalApply(
        @Path("waste_id") waste_id: Int
    ): ApiResponse<Map<String, String>>

    @GET("waste/apply/{waste_id}")
    suspend fun getWasteDisposalApplyDetail(
        @Path("waste_id") waste_id: Int
    ): ApiResponse<WasteDisposalApplyDetailEntity>
}