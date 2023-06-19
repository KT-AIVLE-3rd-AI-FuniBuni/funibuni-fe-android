package com.aivle.domain.usecase.sharing

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class GetSharingPostListUserCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke() = repository.getSharingPostList()
}