package com.aivle.presentation.intro.intro

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.response.SignInWithTokenResponse
import com.aivle.domain.usecase.sign.SignInWithTokenUseCase
import com.aivle.domain.usecase.token.GetRefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "IntroViewModel"

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val GetRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val SignInWithTokenUseCase: SignInWithTokenUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun checkRefreshTokenIfExistsSignIn() {
        viewModelScope.launch {
            Log.d(TAG, "viewModelScope.launch")
            val refreshToken = GetRefreshTokenUseCase()
            Log.d(TAG, "viewModelScope.launch: $refreshToken")
            if (refreshToken == null) {
                Log.d(TAG, "viewModelScope.launch: refreshToken == null")
                _eventFlow.emit(Event.RefreshToken.NotExists)
            } else {
                signInWithToken()
            }
        }
    }

    private suspend fun signInWithToken() {
        SignInWithTokenUseCase()
            .catch { _eventFlow.emit(Event.SignIn.Failure(it.message)) }
            .collect { response -> when (response) {
                is SignInWithTokenResponse.Success -> {
                    _eventFlow.emit(Event.SignIn.Succeed)
                }
                is SignInWithTokenResponse.Failure.ExpiredToken -> {
                    _eventFlow.emit(Event.RefreshToken.Invalid)
                }
                is SignInWithTokenResponse.Failure.InvalidToken -> {
                    _eventFlow.emit(Event.RefreshToken.Expired)
                }
                is SignInWithTokenResponse.Failure.NotFoundUser -> {
                    _eventFlow.emit(Event.SignIn.Failure("Not found user"))
                }
                is SignInWithTokenResponse.Exception -> {
                    _eventFlow.emit(Event.SignIn.Failure(response.message))
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class RefreshToken : Event() {
            object NotExists : RefreshToken()
            object Invalid : RefreshToken()
            object Expired : RefreshToken()
        }
        sealed class SignIn : Event() {
            object Succeed : SignIn()
            data class Failure(val message: String?) : SignIn()
        }
    }
}