package com.aivle.domain.repository

import com.aivle.domain.model.kakao.KakaoAddress
import com.aivle.domain.model.kakao.KakaoCoord2Address
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow

interface KakaoAddressRepository {

    suspend fun searchAddress(query: String, page: Int, size: Int): Flow<DataResponse<KakaoAddress>>

    suspend fun coordinateToAddress(x: Double, y: Double): Flow<DataResponse<KakaoCoord2Address>>
}