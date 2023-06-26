package com.aivle.data.api

import com.aivle.data.entity.kakao.KakaoAddressEntity
import com.aivle.data.entity.kakao.KakaoCoord2AddressEntity
import com.aivle.domain.model.kakao.KakaoCoord2Address
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApi {

    @GET("search/address")
    suspend fun searchAddress(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResponse<KakaoAddressEntity>

    @GET("geo/coord2address")
    suspend fun coordinateToAddress(
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("input_coord") input_coord: String = "WGS84", // 좌표계: WGS84, WCONGNAMUL, CONGNAMUL, WTM, TM (기본값: WGS84)
    ): ApiResponse<KakaoCoord2AddressEntity>
}