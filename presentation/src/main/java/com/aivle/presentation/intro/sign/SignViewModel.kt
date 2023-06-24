package com.aivle.presentation.intro.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignViewModel"

@HiltViewModel
class SignViewModel @Inject constructor() : ViewModel() {

    private val _dataEventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.PhoneNumber(""))
    val dataEventFlow: StateFlow<Event> get() = _dataEventFlow

    var phoneNumber: String = ""
    var userName: String = ""

    fun sendPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _dataEventFlow.emit(Event.PhoneNumber(phoneNumber))
        }
    }

    fun sendSmsCode(smsCode: String) {
        viewModelScope.launch {
            _dataEventFlow.emit(Event.SmsCode(smsCode))
        }
    }

    sealed class Event {
        data class PhoneNumber(val value: String) : Event()
        data class SmsCode(val value: String) : Event()
    }
}