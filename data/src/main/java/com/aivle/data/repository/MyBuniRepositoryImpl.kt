package com.aivle.data.repository

import com.aivle.data.api.MyBuniApi
import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.data.mapper.toModel
import com.aivle.domain.model.mybuni.MyBuni
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.domain.repository.MyBuniRepository
import com.aivle.domain.response.DataResponse
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyBuniRepositoryImpl @Inject constructor(
    @FuniBuniApiQualifier private val api: MyBuniApi
) : MyBuniRepository {

    override suspend fun getMyBuni(): Flow<DataResponse<MyBuni>> = flow {
        api.getMyBuniInfo()
            .suspendOnSuccess {
                val myBuni = data.toModel()
                emit(DataResponse.Success(myBuni))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun getWasteDisposalApplies(): Flow<DataResponse<List<WasteDisposalApplyItem>>> = flow {
        api.getWasteDisposalApplies()
            .suspendOnSuccess {
                val applies = data.mapNotNull { it?.toModel() }
                    .sortedByDescending { it.created_at }

                emit(DataResponse.Success(applies))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun getMySharingPosts(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        api.getMySharingPosts()
            .suspendOnSuccess {
                val postItems = data.map { it.toModel() }
                    .sortedByDescending { it.created_at }

                emit(DataResponse.Success(postItems))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun getMyFavoritePosts(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        api.getMyFavoritePosts()
            .suspendOnSuccess {
                val postItems = data.map { it.toModel() }
                    .sortedByDescending { it.created_at }

                emit(DataResponse.Success(postItems))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun getMyActivities(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        api.getMyActivities()
            .suspendOnSuccess {
                val postItems = data.map { it.toModel() }
                    .sortedByDescending { it.created_at }

                emit(DataResponse.Success(postItems))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }
}