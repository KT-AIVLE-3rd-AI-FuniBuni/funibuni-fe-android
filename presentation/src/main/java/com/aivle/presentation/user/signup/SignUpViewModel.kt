package com.aivle.presentation.user.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.user.SignUpResponse
import com.aivle.domain.model.user.UserForSignUp
import com.aivle.domain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignUpViewModel"

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val SignUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.ShowToastError(""))
    val eventFlow: StateFlow<Event>
        get() = _eventFlow

    fun signUp(
        userId: String,
        userPassword: String,
        name: String,
        phoneNumber: String,
    ) {
        viewModelScope.launch {
            val user = UserForSignUp(userId, userPassword, name, phoneNumber)

            SignUpUseCase(user)
                .catch {
                    _eventFlow.emit(Event.ShowToastError(it.message))
                }
                .collect { response ->
                    when (response) {
                        is SignUpResponse.Success ->
                            _eventFlow.emit(Event.Success)
                        is SignUpResponse.Error.DuplicateID ->
                            _eventFlow.emit(Event.DuplicateIdError)
                        is SignUpResponse.Error.DuplicatePhoneNumber ->
                            _eventFlow.emit(Event.DuplicatePhoneNumberError)
                        is SignUpResponse.Exception ->
                            _eventFlow.emit(Event.ShowToastError(response.message))
                    }
                }

        }
    }

    sealed class Event {
        object Success : Event()
        object DuplicateIdError : Event()
        object DuplicatePhoneNumberError : Event()
        data class ShowToastError(val message: String?) : Event()
    }
}