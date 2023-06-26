package com.aivle.data.repository

import com.aivle.data.mapper.toModel
import com.aivle.data.api.KakaoApi
import com.aivle.data.di.api.KakaoApiProvider
import com.aivle.domain.model.kakao.KakaoAddress
import com.aivle.domain.model.kakao.KakaoCoord2Address
import com.aivle.domain.repository.KakaoAddressRepository
import com.aivle.domain.response.DataResponse
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KakaoAddressRepositoryImpl @Inject constructor(
    @KakaoApiProvider private val api: KakaoApi
) : KakaoAddressRepository {

    override suspend fun searchAddress(
        query: String,
        page: Int,
        size: Int
    ): Flow<DataResponse<KakaoAddress>> = flow {
        api.searchAddress(query, page, size)
            .suspendOnSuccess {
                val address = data.toModel()
                emit(DataResponse.Success(address))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun coordinateToAddress(
        x: Double,
        y: Double
    ): Flow<DataResponse<KakaoCoord2Address>> = flow {
        api.coordinateToAddress(x.toString(), y.toString())
            .suspendOnSuccess {
                val address = data.toModel()
                emit(DataResponse.Success(address))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }
}