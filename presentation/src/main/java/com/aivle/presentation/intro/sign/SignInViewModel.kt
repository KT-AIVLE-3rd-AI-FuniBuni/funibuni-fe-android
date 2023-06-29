package com.aivle.presentation.intro.sign

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.response.SignInResponse
import com.aivle.domain.usecase.sign.SignInUseCase
import com.aivle.presentation.intro.firebase.FirebasePhoneAuth
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignInViewModel"

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val phoneAuth: FirebasePhoneAuth,
    private val SignInUseCase: SignInUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    var authenticatingPhoneNumber: String = ""
        private set

    init {
        phoneAuth.init()
            .setOnPhoneAuthCallback(OnMyPhoneAuthCallback())
    }

    fun send(activity: Activity, phoneNumber: String) {
        phoneAuth.send(activity, phoneNumber.toFirebasePhoneFormat())
        viewModelScope.launch {
            _eventFlow.emit(Event.RequestSms.FirstTry.Loading)
        }
    }

    fun resend(activity: Activity, phoneNumber: String) {
        phoneAuth.resend(activity, phoneNumber.toFirebasePhoneFormat())
        viewModelScope.launch {
            _eventFlow.emit(Event.RequestSms.Retry.Loading)
        }
    }

    fun authenticateAndSignIn(activity: Activity, smsCode: String) {
        viewModelScope.launch {
            phoneAuth.authenticate(activity, smsCode)
        }
    }

    fun isSameAuthenticatingPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber == authenticatingPhoneNumber.replace("-", " ")
    }

    private fun String.toFirebasePhoneFormat(): String {
        return "+82 " + this.replace(" ", "-")
    }

    private fun String.toFuniBuniPhoneFormat(): String {
        return this.drop(4)
    }

    sealed class Event {
        object None : Event()

        sealed class RequestSms : Event() {
            sealed class FirstTry : RequestSms() {
                object Loading : FirstTry()
                object Started : FirstTry()
            }
            sealed class Retry : RequestSms() {
                object Loading : Retry()
                object Started : Retry()
            }
            data class OnVerificationCompleted(val smsCode: String) : Event()
            data class OnVerificationFailed(val message: String?) : Event()
            object IncorrectCode : RequestSms()
            object Timeout : RequestSms()
            data class UpdateTimer(val remainingTime: String) : RequestSms()
            data class Exception(val message: String?) : RequestSms()
        }

        sealed class SignIn : Event() {
            object Succeed : SignIn()
            object NotExistsUser : SignIn()
            data class Exception(val message: String?) : SignIn()
        }

        data class ShowToast(val message: String?) : Event()
    }

    private inner class OnMyPhoneAuthCallback : FirebasePhoneAuth.OnPhoneAuthCallback {
        override fun onVerificationCompleted(smsCode: String) {
            Log.d(TAG, "onVerificationCompleted(): smsCode=$smsCode")

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.OnVerificationCompleted(smsCode))
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.d(TAG, "onVerificationFailed(): e=$e")
            authenticatingPhoneNumber = ""

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.OnVerificationFailed(e.message))
            }
        }

        override fun onStarted(phoneNumber: String) {
            Log.d(TAG, "onStarted(): $phoneNumber")
            authenticatingPhoneNumber = phoneNumber.toFuniBuniPhoneFormat()

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.FirstTry.Started)
            }
        }

        override fun onReStarted(phoneNumber: String) {
            Log.d(TAG, "onReStarted(): $phoneNumber")
            authenticatingPhoneNumber = phoneNumber.toFuniBuniPhoneFormat()

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.Retry.Started)
            }
        }

        override fun onTimer(elapsedTimeSec: Int, remainingTimeString: String) {
            Log.d(TAG, "onTimer($elapsedTimeSec): $remainingTimeString")

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.UpdateTimer(remainingTimeString))
            }
        }

        override fun onTimeout() {
            Log.d(TAG, "onTimeout()")
            authenticatingPhoneNumber = ""

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.Timeout)
            }
        }

        override fun onPhoneAuthSucceed() {
            Log.d(TAG, "onPhoneAuthSucceed()")

            viewModelScope.launch {
                val signInUser = SignInUser(authenticatingPhoneNumber)
                SignInUseCase(signInUser)
                    .catch {
                        _eventFlow.emit(Event.ShowToast(it.message))
                    }
                    .collect { response -> when (response) {
                        is SignInResponse.Success -> _eventFlow.emit(Event.SignIn.Succeed)
                        is SignInResponse.Error.NotFoundUser -> _eventFlow.emit(Event.SignIn.NotExistsUser)
                        is SignInResponse.Exception -> _eventFlow.emit(
                            Event.SignIn.Exception(
                                response.message
                            )
                        )
                    }}
            }
        }

        override fun onPhoneAuthFailed() {
            Log.d(TAG, "onPhoneAuthFailed()")

            viewModelScope.launch {
                _eventFlow.emit(Event.RequestSms.IncorrectCode)
            }
        }
    }
}