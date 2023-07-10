package com.aivle.presentation.sharing.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.sharingPost.AddCommentUseCase
import com.aivle.domain.usecase.sharingPost.CompleteSharingPostUseCase
import com.aivle.domain.usecase.sharingPost.DeleteCommentUseCase
import com.aivle.domain.usecase.sharingPost.DeletePostUseCase
import com.aivle.domain.usecase.sharingPost.GetSharingPostUseCase
import com.aivle.domain.usecase.sharingPost.LikePostUseCase
import com.aivle.domain.usecase.sharingPost.UnlikePostUseCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingPostDetailViewModel @Inject constructor(
    private val getSharingPostUseCase: GetSharingPostUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val completeSharingPostUseCase: CompleteSharingPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    var postDetail: SharingPostDetail? = null
        private set

    val postId: Int?
        get() = postDetail?.post_id

    /** 게시물 상세정보 불러오기 */
    fun loadSharingPostDetail(postId: Int) = launchDefault {
        getSharingPostUseCase(postId)
            .catch {
                _eventFlow.emit(Event.Failure(it.message))
            }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    postDetail = response.data
                    _eventFlow.emit(Event.LoadPost.Success(postDetail!!))
                    _eventFlow.emit(Event.None)
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    /** 댓글 추가 */
    fun addComment(comment: String) = launchDefault {
        val postId = postDetail?.post_id ?: return@launchDefault
        addCommentUseCase(postId, comment)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is DataResponse.Success -> {
                    val newComment = response.data
                    val oldPostDetail = postDetail!!
                    val newPostDetail = oldPostDetail.copy(
                        comments = oldPostDetail.comments + newComment
                    )
                    postDetail = newPostDetail

                    _eventFlow.emit(Event.AddComment.Success(newPostDetail.comments))
                }
            }}
    }

    /** 댓글 삭제 */
    fun deleteComment(commentId: Int) = launchDefault {
        val postId = postId ?: return@launchDefault

        deleteCommentUseCase(postId, commentId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is NothingResponse.Success -> {
                    val oldPostDetail = postDetail!!
                    val newPostDetail = oldPostDetail.copy(
                        comments = oldPostDetail.comments.filterNot { it.comment_id == commentId }
                    )
                    postDetail = newPostDetail

                    _eventFlow.emit(Event.DeleteComment.Success(newPostDetail.comments))
                }
            }}
    }

    /** 좋아요 */
    fun likePost() = launchDefault {
        val postId = postId ?: return@launchDefault

        likePostUseCase(postId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Success -> {
                }
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    /** 좋아요 취소 */
    fun unlikePost() = launchDefault {
        val postId = postId ?: return@launchDefault

        unlikePostUseCase(postId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Success -> {
                }
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }
    /** 나눔 완료 */
    fun completeSharingPost() = launchDefault {
        val postId = postId ?: return@launchDefault

        completeSharingPostUseCase(postId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is NothingResponse.Success -> {
                    _eventFlow.emit(Event.CompleteSharingPost.Success)
                }
            }}
    }

    /** 게시물 삭제 */
    fun deletePost() = launchDefault {
        val postId = postId ?: return@launchDefault

        deletePostUseCase(postId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is NothingResponse.Success -> {
                    _eventFlow.emit(Event.DeletePost.Success)
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class LoadPost : Event() {
            data class Success(val postDetail: SharingPostDetail) : LoadPost()
        }
        sealed class AddComment : Event() {
            data class Success(val comments: List<Comment>) : AddComment()
        }
        sealed class DeleteComment : Event() {
            data class Success(val comments: List<Comment>) : DeleteComment()
        }
        sealed class CompleteSharingPost : Event() {
            object Success : CompleteSharingPost()
        }
        sealed class DeletePost : Event() {
            object Success : DeletePost()
        }
        data class Failure(val message: String?) : Event()
    }
}