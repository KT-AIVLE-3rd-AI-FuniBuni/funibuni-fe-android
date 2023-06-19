package com.aivle.presentation.user.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sign.SignUp
import com.aivle.domain.response.SignUpResponse
import com.aivle.domain.usecase.sign.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val SignUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun signUp(phoneNumber: String, name: String) {
        viewModelScope.launch {
            SignUpUseCase(SignUp(phoneNumber, name))
                .catch {
                    _eventFlow.emit(Event.Failure.Exception(it.message))
                }
                .collect { response -> when (response) {
                    is SignUpResponse.Success -> _eventFlow.emit(Event.Success)
                    is SignUpResponse.Error -> _eventFlow.emit(Event.Failure.Error)
                    is SignUpResponse.Exception -> _eventFlow.emit(Event.Failure.Exception(response.message))
                }}
        }
    }

    sealed class Event {
        object None : Event()
        object Success : Event()
        sealed class Failure : Event() {
            object Error : Failure()
            data class Exception(val message: String?) : Failure()
        }
    }
}