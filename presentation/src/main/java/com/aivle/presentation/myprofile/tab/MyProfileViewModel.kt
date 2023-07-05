package com.aivle.presentation.myprofile.tab

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.mybuni.MyBuni
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.myProfile.GetMyBuniUseCase
import com.aivle.domain.usecase.myProfile.GetMyProfileDetailUseCase
import com.aivle.domain.usecase.user.UserInfo
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getMyBuniUseCase: GetMyBuniUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadMyBuni() = launchDefault {
        getMyBuniUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
                is DataResponse.Success -> {
                    val data = response.data
                    _eventFlow.emit(Event.LoadMyBuni.Success(data))
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class LoadMyBuni : Event() {
            data class Success(val data: MyBuni) : LoadMyBuni()
        }
        data class Failure(val message: String?) : Event()
    }
}