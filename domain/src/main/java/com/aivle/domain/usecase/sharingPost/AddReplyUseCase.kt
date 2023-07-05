package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class AddReplyUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(postId: Int, commentId: Int, reply: String) =
        repository.addReply(postId, commentId, reply)
}