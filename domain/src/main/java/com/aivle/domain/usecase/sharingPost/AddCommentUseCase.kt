package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(postId: Int, comment: String) =
        repository.addComment(postId, comment)
}