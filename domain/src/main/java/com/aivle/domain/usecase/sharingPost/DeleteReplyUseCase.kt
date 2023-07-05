package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class DeleteReplyUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int, replyId: Int) =
        repository.deleteReply(postId, commentId, replyId)
}