package com.aivle.domain.usecase.myProfile

import com.aivle.domain.model.user.User
import com.aivle.domain.repository.UserRepository
import javax.inject.Inject

class UpdateMyProfileDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) = repository.updateUserInfo(user)
}