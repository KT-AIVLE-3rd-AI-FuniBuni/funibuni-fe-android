package com.aivle.domain.usecase.kakao

import com.aivle.domain.repository.KakaoAddressRepository
import javax.inject.Inject

class GetKakaoCoord2AddressUseCase @Inject constructor(
    private val repository: KakaoAddressRepository
) {
    suspend operator fun invoke(x: Double, y: Double) = repository.coordinateToAddress(x, y)
}