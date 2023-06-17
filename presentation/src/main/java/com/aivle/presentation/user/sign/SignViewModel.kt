package com.aivle.presentation.user.sign

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignViewModel"

@HiltViewModel
class SignViewModel @Inject constructor(
    private val SignUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _dataEventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.PhoneNumber(""))
    val dataEventFlow: StateFlow<Event> get() = _dataEventFlow

    fun setPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _dataEventFlow.emit(Event.PhoneNumber(phoneNumber))
        }
    }

    fun setSmsCode(smsCode: String) {
        Log.d(TAG, "setSmsCode(): $smsCode")
        viewModelScope.launch {
            _dataEventFlow.emit(Event.SmsCode(smsCode))
        }
    }

    fun signIn() {

    }

    fun signUp() {

    }

    sealed class Event {
        data class PhoneNumber(val value: String) : Event()
        data class SmsCode(val value: String) : Event()
    }
}