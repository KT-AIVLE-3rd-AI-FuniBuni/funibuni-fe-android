package com.aivle.presentation.user.sign

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
class SignUpViewModel2 @Inject constructor(
    private val SignUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.ShowToastError(null))
    val eventFlow: StateFlow<Event>
        get() = _eventFlow

    var userId: String? = null
        private set

    var phoneNumber: String? = null
        private set

    fun signUp(
        userId: String,
        userPassword: String,
        name: String,
        phoneNumber: String,
    ) {
        this.userId = userId
        this.phoneNumber = phoneNumber

        viewModelScope.launch {
            val user = UserForSignUp(name, phoneNumber)

            SignUpUseCase(user)
                .catch {
                    _eventFlow.emit(Event.ShowToastError(it.message))
                }
                .collect { response ->
                    when (response) {
                        is SignUpResponse.Success ->
                            _eventFlow.emit(Event.Success)

                        is SignUpResponse.Error.DuplicateID ->
                            _eventFlow.emit(Event.Error.DuplicateUserID)

                        is SignUpResponse.Error.DuplicatePhoneNumber ->
                            _eventFlow.emit(Event.Error.DuplicatePhoneNumber)

                        is SignUpResponse.Exception ->
                            _eventFlow.emit(Event.ShowToastError(response.message))
                    }
                }
        }
    }

    private fun verify(
        userId: String,
        userPassword: String,
        name: String,
        phoneNumber: String,
    ) {

    }

    sealed class Event {
        object Success : Event()

        sealed class Required {
            object UserID : Required()
            object Password : Required()
            object PasswordConfirm : Required()
            object UserName : Required()
            object PhoneNumber : Required()
        }

        sealed class Error {
            object DuplicateUserID : Event()
            object DuplicatePhoneNumber : Event()
        }

        data class ShowToastError(val message: String?) : Event()
    }
}