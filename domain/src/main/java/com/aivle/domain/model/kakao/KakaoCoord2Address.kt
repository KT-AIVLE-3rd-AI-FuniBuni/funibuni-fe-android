package com.aivle.domain.model.kakao

data class KakaoCoord2Address(
    val meta: KakaoCoord2AddressMeta,                 // 응답 관련 정보
    val documents: List<KakaoCoord2AddressDocument>,  // 응답 결과
)

data class KakaoCoord2AddressMeta(
    val total_count: Int,       // 현재 페이지가 마지막 페이지인지 여부. 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
)

data class KakaoCoord2AddressDocument(
    val road_address: KakaoCoord2RoadAddress? = null, // 도로명 주소 상세 정보
    val land_address: KakaoCoord2LandAddress? = null, // 지번 주소 상세 정보
)

data class KakaoCoord2RoadAddress(
    val address_name: String,	        // 전체 도로명 주소
    val region_1depth_name: String,	    // 지역명1
    val region_2depth_name: String,	    // 지역명2
    val region_3depth_name: String,	    // 지역명3
    val road_name: String,	            // 도로명
    val underground_yn: String,	        // 지하 여부, Y 또는 N
    val main_building_no: String,	    // 건물 본번
    val sub_building_no: String,	    // 건물 부번, 없을 경우 빈 문자열("") 반환
    val building_name: String,	        // 건물 이름
    val zone_no: String,	            // 우편번호(5자리)
)

data class KakaoCoord2LandAddress(
    val address_name: String,           // 전체 지번 주소
    val region_1depth_name:	String,     // 지역 1 Depth, 시도 단위
    val region_2depth_name:	String,     // 지역 2 Depth, 구 단위
    val region_3depth_name:	String,     // 지역 3 Depth, 동 단위
    val mountain_yn: String,            // 산 여부, Y 또는 N
    val main_address_no: String,        // 지번 주번지
    val sub_address_no:	String,         // 지번 부번지, 없을 경우 빈 문자열("") 반환
)