package com.aivle.presentation.sharing.tab

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.sharingPost.GetSharingPostListUserCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class SharingPostListViewModel @Inject constructor(
    private val getSharingPostListUseCase: GetSharingPostListUserCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadSharingPosts(district: String) = launchDefault {
        getSharingPostListUseCase(district)
            .catch {
                _eventFlow.emit(Event.LoadPosts.Failure(it.message))
            }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    val posts = response.data
                    if (posts.isEmpty()) {
                        _eventFlow.emit(Event.LoadPosts.Empty)
                    } else {
                        _eventFlow.emit(Event.LoadPosts.Success(posts))
                    }
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.LoadPosts.Failure(response.message))
                }
            }}
    }

    sealed class Event {
        object None : Event()

        sealed class LoadPosts : Event() {
            object Empty : LoadPosts()
            data class Success(val posts: List<SharingPostItem>) : LoadPosts()
            data class Failure(val message: String?) : LoadPosts()
        }
    }
}