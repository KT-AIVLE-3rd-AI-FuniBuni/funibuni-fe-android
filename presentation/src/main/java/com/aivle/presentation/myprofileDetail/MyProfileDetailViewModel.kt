package com.aivle.presentation.myprofileDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.user.User
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.myProfile.GetMyProfileDetailUseCase
import com.aivle.domain.usecase.sign.SignOutUseCase
import com.aivle.domain.usecase.sign.WithdrawalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyProfileDetailViewModel"

@HiltViewModel
class MyProfileDetailViewModel @Inject constructor(
    private val SignOutUseCase: SignOutUseCase,
    private val WithdrawalUseCase: WithdrawalUseCase,
    private val GetMyProfileDetailUseCase: GetMyProfileDetailUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadMyProfileDetail() {
        viewModelScope.launch {
            GetMyProfileDetailUseCase()
                .catch { _eventFlow.emit(Event.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        _eventFlow.emit(Event.MyProfile(response.data))
                    }
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    fun signOut() {
        viewModelScope.launch {
            SignOutUseCase()
                .catch {
                    _eventFlow.emit(Event.Failure(it.message))
                }
                .collect { response -> when (response) {
                    is NothingResponse.Success -> {
                        _eventFlow.emit(Event.SignOut)
                    }
                    is NothingResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            WithdrawalUseCase()
                .catch {
                    _eventFlow.emit(Event.Failure(it.message))
                }
                .collect { response -> when (response) {
                    is NothingResponse.Success -> {
                        _eventFlow.emit(Event.Withdrawal)
                    }
                    is NothingResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    sealed class Event {
        object None: Event()
        data class MyProfile(val myProfile: User) : Event()
        object SignOut : Event()
        object Withdrawal : Event()
        data class Failure(val message: String?) : Event()
    }
}