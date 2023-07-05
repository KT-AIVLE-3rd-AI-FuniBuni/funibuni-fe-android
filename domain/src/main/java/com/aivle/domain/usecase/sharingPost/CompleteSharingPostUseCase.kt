package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class CompleteSharingPostUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(post_id: Int) = repository.completeSharingPost(post_id)
}