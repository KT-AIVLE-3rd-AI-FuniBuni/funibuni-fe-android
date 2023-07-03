package com.aivle.presentation.myprofile.postList

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.myProfile.GetMyActivityListUseCase
import com.aivle.domain.usecase.myProfile.GetMyFavoritePostListUseCase
import com.aivle.domain.usecase.myProfile.GetMySharingPostListUseCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MySharingPostListViewModel @Inject constructor(
    private val getMySharingPostListUseCase: GetMySharingPostListUseCase,
    private val getMyFavoritePostListUseCase: GetMyFavoritePostListUseCase,
    private val getMyActivityListUseCase: GetMyActivityListUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadMySharingPosts() = launchDefault {
        getMySharingPostListUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    _eventFlow.emit(Event.LoadPostList.Success(response.data))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    fun loadMyFavoritePosts() = launchDefault {
        getMyFavoritePostListUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    _eventFlow.emit(Event.LoadPostList.Success(response.data))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    fun loadMyActivities() = launchDefault {
        getMyActivityListUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    _eventFlow.emit(Event.LoadPostList.Success(response.data))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class LoadPostList : Event() {
            data class Success(val posts: List<SharingPostItem>) : LoadPostList()
        }
        data class Failure(val message: String?) : Event()
    }
}