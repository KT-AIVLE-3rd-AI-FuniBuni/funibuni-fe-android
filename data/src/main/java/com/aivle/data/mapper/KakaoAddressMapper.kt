package com.aivle.data.mapper

import com.aivle.data.entity.kakao.KakaoAddressDocumentEntity
import com.aivle.data.entity.kakao.KakaoAddressEntity
import com.aivle.data.entity.kakao.KakaoAddressMetaEntity
import com.aivle.data.entity.kakao.KakaoLandAddressEntity
import com.aivle.data.entity.kakao.KakaoRoadAddressEntity
import com.aivle.domain.model.kakao.KakaoAddress
import com.aivle.domain.model.kakao.KakaoAddressDocument
import com.aivle.domain.model.kakao.KakaoAddressMeta
import com.aivle.domain.model.kakao.KakaoLandAddress
import com.aivle.domain.model.kakao.KakaoRoadAddress

internal fun KakaoAddressEntity.toModel() = KakaoAddress(
    meta.toModel(),
    documents.map { it.toModel() },
)

internal fun KakaoAddressDocumentEntity.toModel() = KakaoAddressDocument(
    address_name, address_type, x, y, road_address?.toModel(), land_address?.toModel()
)

internal fun KakaoRoadAddressEntity.toModel() = KakaoRoadAddress(
    address_name, region_1depth_name, region_2depth_name, region_3depth_name, road_name, underground_yn, main_building_no, sub_building_no, building_name, zone_no, x, y
)

internal fun KakaoLandAddressEntity.toModel() = KakaoLandAddress(
    address_name, region_1depth_name, region_2depth_name, region_3depth_name, region_3depth_h_name, h_code, b_code, mountain_yn, main_address_no, sub_address_no, x, y
)

internal fun KakaoAddressMetaEntity.toModel() = KakaoAddressMeta(
    is_end, pageable_count, total_count
)