package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int) =
        repository.deleteComment(postId, commentId)
}