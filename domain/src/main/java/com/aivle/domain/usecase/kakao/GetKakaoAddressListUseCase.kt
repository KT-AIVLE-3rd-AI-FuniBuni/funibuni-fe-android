package com.aivle.domain.usecase.kakao

import com.aivle.domain.repository.KakaoAddressRepository
import javax.inject.Inject

class GetKakaoAddressListUseCase @Inject constructor(
    private val repository: KakaoAddressRepository
) {
    suspend operator fun invoke(query: String, page: Int, size: Int) =
        repository.searchAddress(query, page, size)
}