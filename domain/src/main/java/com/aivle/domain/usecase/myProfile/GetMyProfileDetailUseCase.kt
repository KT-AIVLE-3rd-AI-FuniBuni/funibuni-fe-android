package com.aivle.domain.usecase.myProfile

import com.aivle.domain.repository.UserRepository
import javax.inject.Inject

class GetMyProfileDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getUserInfo()
}