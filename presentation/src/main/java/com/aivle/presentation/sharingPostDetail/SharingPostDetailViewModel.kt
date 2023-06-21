package com.aivle.presentation.sharingPostDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.sharingPost.GetSharingPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingPostDetailViewModel @Inject constructor(
    private val GetSharingPostUseCase: GetSharingPostUseCase
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadSharingPostDetail(postId: Int) {
        viewModelScope.launch {
            GetSharingPostUseCase(postId)
                .catch { _eventFlow.emit(Event.LoadPost.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> _eventFlow.emit(Event.LoadPost.Success(response.data))
                    is DataResponse.Failure -> _eventFlow.emit(Event.LoadPost.Failure(response.message))
                }}
        }
    }

    sealed class Event {
        object None : Event()

        sealed class LoadPost : Event() {
            data class Success(val postDetail: SharingPostDetail) : LoadPost()
            data class Failure(val message: String?): LoadPost()
        }
    }
}