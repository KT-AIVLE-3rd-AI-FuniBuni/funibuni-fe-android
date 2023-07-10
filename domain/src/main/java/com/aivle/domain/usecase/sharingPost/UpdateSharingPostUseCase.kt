package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class UpdateSharingPostUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(post: SharingPostDetail) = repository.updatePost(post)
}