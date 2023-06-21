package com.aivle.presentation.sharing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.sharingPost.GetSharingPostListUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharingPostListViewModel @Inject constructor(
    private val GetSharingPostListUseCase: GetSharingPostListUserCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadSharingPosts() {
        viewModelScope.launch {
            GetSharingPostListUseCase()
                .catch {
                    _eventFlow.emit(Event.Failure(it.message))
                }
                .collect { response -> when (response) {
                    is DataResponse.Success -> _eventFlow.emit(Event.Success(response.data))
                    is DataResponse.Failure -> _eventFlow.emit(Event.Failure(response.message))
                }}
        }
    }

    sealed class Event {
        object None : Event()
        data class Success(val posts: List<SharingPostItem>) : Event()
        data class Failure(val message: String?) : Event()
    }
}