package com.aivle.presentation.intro.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.kakao.KakaoAddressDocument
import com.aivle.domain.model.sign.SignUpUser
import com.aivle.domain.response.SignUpResponse
import com.aivle.domain.usecase.sign.SignUpUseCase
import com.loggi.core_util.extensions.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignViewModel @Inject constructor(
    private val SignUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _signUpEventFlow: MutableSharedFlow<SignUpEvent> = MutableSharedFlow()
    val signUpEventFlow: SharedFlow<SignUpEvent> get() = _signUpEventFlow

    private val _dataEventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.PhoneNumber(""))
    val dataEventFlow: StateFlow<Event> get() = _dataEventFlow

    private val _addressEventFlow: MutableSharedFlow<KakaoAddressDocument> = MutableSharedFlow()
    val addressEventFlow: SharedFlow<KakaoAddressDocument> get() = _addressEventFlow

    var phoneNumber: String = ""
    var userName: String = ""
    var address: KakaoAddressDocument? = null

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

    fun sendAddress(address: KakaoAddressDocument) {
        this.address = address
        viewModelScope.launch {
            _addressEventFlow.emit(address)
        }
    }

    fun signUp(addressDetail: String) {
        log("signUp(): phoneNumber=$phoneNumber, userName=$userName, address=$address, addressDetail=$addressDetail")
        viewModelScope.launch {
            val signUpUser = SignUpUser(phoneNumber, userName, address!!, addressDetail)

            SignUpUseCase(signUpUser)
                .catch { _signUpEventFlow.emit(SignUpEvent.Failure(it.message)) }
                .collect { response -> when (response) {
                    is SignUpResponse.Success -> _signUpEventFlow.emit(SignUpEvent.Success)
                    is SignUpResponse.Failure -> _signUpEventFlow.emit(SignUpEvent.Failure(response.message))
                }}
        }
    }

    sealed class Event {
        data class PhoneNumber(val value: String) : Event()
        data class SmsCode(val value: String) : Event()
    }

    sealed class SignUpEvent {
        object Success : SignUpEvent()
        data class Failure(val message: String?) : SignUpEvent()
    }
}