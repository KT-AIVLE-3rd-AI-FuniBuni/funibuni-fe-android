package com.aivle.presentation.sharing.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.sharingPost.AddReplyUseCase
import com.aivle.domain.usecase.sharingPost.DeleteReplyUseCase
import com.aivle.domain.usecase.sharingPost.GetReplyListUseCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyBottomSheetViewModel @Inject constructor(
    private val getReplyListUseCase: GetReplyListUseCase,
    private val addReplyUseCase: AddReplyUseCase,
    private val deleteReplyUseCase: DeleteReplyUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    var replies: List<Reply>? = null
        private set

    fun loadReplies(postId: Int, commentId: Int) = launchDefault {
        getReplyListUseCase(postId, commentId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is DataResponse.Success -> {
                    replies = response.data
                    _eventFlow.emit(Event.LoadReplies.Success(replies!!))
                }
            }}

    }

    fun addReply(postId: Int, commentId: Int, content: String) = launchDefault {
        addReplyUseCase(postId, commentId, content)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is DataResponse.Success -> {
                    replies = (replies ?: emptyList()) + response.data
                    _eventFlow.emit(Event.AddReply.Success(replies!!))
                }
            }}
    }

    fun deleteReply(postId: Int, commentId: Int, replyId: Int) = launchDefault {
        deleteReplyUseCase(postId, commentId, replyId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is NothingResponse.Success -> {
                    replies = replies?.filterNot { it.reply_id == replyId }
                    if (replies != null) {
                        _eventFlow.emit(Event.DeleteReply.Success(replies!!))
                    }
                }
            }}
    }

    sealed class Event {
        object None : Event()

        sealed class LoadReplies : Event() {
            data class Success(val replies: List<Reply>) : LoadReplies()
        }
        sealed class AddReply : Event() {
            data class Success(val replies: List<Reply>) : AddReply()
        }
        sealed class DeleteReply : Event() {
            data class Success(val replies: List<Reply>) : DeleteReply()
        }
        data class Failure(val message: String?): Event()
    }
}