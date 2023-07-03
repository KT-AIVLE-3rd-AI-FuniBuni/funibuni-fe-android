package com.aivle.data.api

import com.aivle.data.entity.sharingPost.SharingPostItemEntity
import com.aivle.data.entity.waste.WasteDisposalApplyItemEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface MyBuniApi {

//    @GET("myburni")
//    suspend fun getMyBuniInfo(): ApiResponse<>

    @GET("myburni/waste")
    suspend fun getWasteDisposalApplies(): ApiResponse<List<WasteDisposalApplyItemEntity>>

    @GET("myburni/posts")
    suspend fun getMySharingPosts(): ApiResponse<List<SharingPostItemEntity>>

    @GET("myburni/like-posts")
    suspend fun getMyFavoritePosts(): ApiResponse<List<SharingPostItemEntity>>

    @GET("myburni/activities")
    suspend fun getMyActivities(): ApiResponse<List<SharingPostItemEntity>>
}