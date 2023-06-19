package com.aivle.domain.repository

import com.aivle.domain.model.SharingPostItem
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow

interface SharingPostRepository {

    fun getSharingPostList(): Flow<DataResponse<List<SharingPostItem>>>
}