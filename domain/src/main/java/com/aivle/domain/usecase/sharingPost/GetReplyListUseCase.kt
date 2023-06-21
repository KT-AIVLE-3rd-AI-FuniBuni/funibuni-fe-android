package com.aivle.domain.usecase.sharingPost

import com.aivle.domain.repository.SharingPostRepository
import javax.inject.Inject

class GetReplyListUseCase @Inject constructor(
    private val repository: SharingPostRepository
) {
    suspend operator fun invoke(commentId: Int) = repository.getReplyList(commentId)
}