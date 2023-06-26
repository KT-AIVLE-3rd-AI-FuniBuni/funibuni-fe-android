package com.aivle.domain.model.kakao

data class KakaoAddress(
    val meta: KakaoAddressMeta,                 // 응답 관련 정보
    val documents: List<KakaoAddressDocument>,  // 응답 결과
)

data class KakaoAddressMeta(
    val is_end: Boolean,        // 	검색어에 검색된 문서 수
    val pageable_count: Int,    // total_count 중 노출 가능 문서 수 (최대: 45)
    val total_count: Int,       // 현재 페이지가 마지막 페이지인지 여부. 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
)

data class KakaoAddressDocument(
    val address_name: String, // 전체 지번 주소 or 전체 도로명 주소, 입력에 따라 결정됨
    val address_type: String, // address_name의 값의 타입(Type) 중 하나: REGION(지명), ROAD(도로명), REGION_ADDR(지번 주소), ROAD_ADDR(도로명 주소)
    val x: String,
    val y: String,
    val road_address: KakaoRoadAddress? = null, // 도로명 주소 상세 정보
    val land_address: KakaoLandAddress? = null, // 지번 주소 상세 정보
) {
    val road_address_name: String
        get() = road_address?.address_name ?: ""

    var onClick: ((address: KakaoAddressDocument) -> Unit)? = null
}

data class KakaoRoadAddress(
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
    val x: String,	                    // X 좌표값, 경위도인 경우 경도(longitude)
    val y: String,	                    // Y 좌표값, 경위도인 경우 위도(latitude)
)

data class KakaoLandAddress(
    val address_name: String,           // 전체 지번 주소
    val region_1depth_name:	String,     // 지역 1 Depth, 시도 단위
    val region_2depth_name:	String,     // 지역 2 Depth, 구 단위
    val region_3depth_name:	String,     // 지역 3 Depth, 동 단위
    val region_3depth_h_name: String,   // 지역 3 Depth, 행정동 명칭
    val h_code:	String,                 // 행정 코드
    val b_code:	String,                 // 법정 코드
    val mountain_yn: String,            // 산 여부, Y 또는 N
    val main_address_no: String,        // 지번 주번지
    val sub_address_no:	String,         // 지번 부번지, 없을 경우 빈 문자열("") 반환
    val x: String,                      // X 좌표값, 경위도인 경우 경도(longitude)
    val y: String,                      // Y 좌표값, 경위도인 경우 위도(latitude)
)