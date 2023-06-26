package com.aivle.data.mapper

import com.aivle.data.entity.kakao.KakaoCoord2AddressDocumentEntity
import com.aivle.data.entity.kakao.KakaoCoord2AddressEntity
import com.aivle.data.entity.kakao.KakaoCoord2AddressMetaEntity
import com.aivle.data.entity.kakao.KakaoCoord2LandAddressEntity
import com.aivle.data.entity.kakao.KakaoCoord2RoadAddressEntity
import com.aivle.domain.model.kakao.KakaoCoord2Address
import com.aivle.domain.model.kakao.KakaoCoord2AddressDocument
import com.aivle.domain.model.kakao.KakaoCoord2AddressMeta
import com.aivle.domain.model.kakao.KakaoCoord2LandAddress
import com.aivle.domain.model.kakao.KakaoCoord2RoadAddress

internal fun KakaoCoord2AddressEntity.toModel() = KakaoCoord2Address(
    meta.toModel(), documents.map { it.toModel() }
)

internal fun KakaoCoord2AddressMetaEntity.toModel() = KakaoCoord2AddressMeta(
    total_count
)

internal fun KakaoCoord2AddressDocumentEntity.toModel() = KakaoCoord2AddressDocument(
    road_address?.toModel(),
    land_address?.toModel(),
)

internal fun KakaoCoord2RoadAddressEntity.toModel() = KakaoCoord2RoadAddress(
    address_name, region_1depth_name, region_2depth_name, region_3depth_name, road_name, underground_yn, main_building_no, sub_building_no, building_name, zone_no
)

internal fun KakaoCoord2LandAddressEntity.toModel() = KakaoCoord2LandAddress(
    address_name, region_1depth_name, region_2depth_name, region_3depth_name, mountain_yn, main_address_no, sub_address_no
)