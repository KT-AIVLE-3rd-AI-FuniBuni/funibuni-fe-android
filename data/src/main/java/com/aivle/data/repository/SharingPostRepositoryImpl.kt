package com.aivle.data.repository

import com.aivle.data._util.SampleData
import com.aivle.data.service.SharingPostService
import com.aivle.domain.model.SharingPostItem
import com.aivle.domain.model.user.User
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class SharingPostRepositoryImpl @Inject constructor(
    private val service: SharingPostService
) : SharingPostRepository {

    override fun getSharingPostList(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        val data = SampleData.getSharingPostItems(20)
        emit(DataResponse.Success(data))
    }
}