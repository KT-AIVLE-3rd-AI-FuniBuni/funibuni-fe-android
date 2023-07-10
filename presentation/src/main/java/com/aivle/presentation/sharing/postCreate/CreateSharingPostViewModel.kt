package com.aivle.presentation.sharing.postCreate

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.sharingPost.SharingPostCreate
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.sharingPost.CreateSharingPostUseCase
import com.aivle.presentation.util.ext.launchDefault
import com.loggi.core_util.extensions.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class CreateSharingPostViewModel @Inject constructor(
    private val createSharingPostUseCase: CreateSharingPostUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun createSharingPost(newPost: SharingPostCreate) = launchDefault {
        log("createSharingPost(): $newPost")
        createSharingPostUseCase(newPost)
            .catch {
                _eventFlow.emit(Event.Failure(it.message))
            }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    _eventFlow.emit(Event.CreatePost.Success(response.data.post_id))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class CreatePost : Event() {
            data class Success(val newPostId: Int) : CreatePost()
        }
        data class Failure(val message: String?) : Event()
    }
}