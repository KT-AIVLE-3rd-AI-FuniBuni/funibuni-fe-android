package com.aivle.presentation.sharing.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.sharingPost.GetReplyListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyBottomSheetViewModel @Inject constructor(
    private val GetReplyListUseCase: GetReplyListUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadReplies(commentId: Int) {
        viewModelScope.launch {
            GetReplyListUseCase(commentId)
                .catch { _eventFlow.emit(Event.LoadReplies.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        _eventFlow.emit(Event.LoadReplies.Success(response.data))
                    }
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.LoadReplies.Failure(response.message))
                    }
                }}
        }
    }

    fun addReply(content: String) {

    }

    sealed class Event {
        object None : Event()

        sealed class LoadReplies : Event() {
            data class Success(val replies: List<Reply>) : LoadReplies()
            data class Failure(val message: String?): LoadReplies()
        }
    }
}