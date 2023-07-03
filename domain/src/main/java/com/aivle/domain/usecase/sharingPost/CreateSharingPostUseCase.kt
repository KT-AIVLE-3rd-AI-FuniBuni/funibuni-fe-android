package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.model.sharingPost.SharingPostCreate
import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class CreateSharingPostUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(post: SharingPostCreate) = repository.createPost(post)
}