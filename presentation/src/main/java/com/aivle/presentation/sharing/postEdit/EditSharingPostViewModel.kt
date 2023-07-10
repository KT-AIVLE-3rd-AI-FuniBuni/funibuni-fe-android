package com.aivle.presentation.sharing.postEdit

import android.util.Log
import androidx.lifecycle.ViewModel
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.sharingPost.GetSharingPostUseCase
import com.aivle.domain.usecase.sharingPost.UpdateSharingPostUseCase
import com.aivle.presentation.util.ext.launchDefault
import com.loggi.core_util.extensions.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class EditSharingPostViewModel @Inject constructor(
    private val getSharingPostUseCase: GetSharingPostUseCase,
    private val updateSharingPostUseCase: UpdateSharingPostUseCase,
) : ViewModel() {

    private val _eventFlow = MutableStateFlow<Event>(Event.None)
    val eventFlow = _eventFlow.asStateFlow()

    var post: SharingPostDetail? = null
        private set

    fun loadPost(postId: Int) = launchDefault {
        getSharingPostUseCase(postId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is DataResponse.Success -> {
                    post = response.data
                    _eventFlow.emit(Event.LoadPost.Success(post!!))
                }
            }}
    }

    fun updatePost(title: String, content: String, expiredDate: String) = launchDefault {
        val newPost = post?.copy(
            title = title,
            content = content,
            expired_date = expiredDate,
        ) ?: return@launchDefault

        log("updatePost(): $newPost")

        updateSharingPostUseCase(newPost)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is NothingResponse.Success -> {
                    _eventFlow.emit(Event.UpdatePost.Success)
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class LoadPost : Event() {
            data class Success(val post: SharingPostDetail) : LoadPost()
        }
        sealed class UpdatePost : Event() {
            object Success : UpdatePost()
        }
        data class Failure(val message: String?) : Event()
    }
}