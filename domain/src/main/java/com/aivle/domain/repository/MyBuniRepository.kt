package com.aivle.domain.repository

import com.aivle.domain.model.mybuni.MyBuni
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow

interface MyBuniRepository {

    suspend fun getMyBuni(): Flow<DataResponse<MyBuni>>

    suspend fun getWasteDisposalApplies(): Flow<DataResponse<List<WasteDisposalApplyItem>>>

    suspend fun getMySharingPosts(): Flow<DataResponse<List<SharingPostItem>>>

    suspend fun getMyFavoritePosts(): Flow<DataResponse<List<SharingPostItem>>>

    suspend fun getMyActivities(): Flow<DataResponse<List<SharingPostItem>>>
}