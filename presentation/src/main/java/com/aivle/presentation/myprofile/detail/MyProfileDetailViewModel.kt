package com.aivle.presentation.myprofile.detail

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.user.User
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.myProfile.GetMyProfileDetailUseCase
import com.aivle.domain.usecase.myProfile.UpdateMyProfileDetailUseCase
import com.aivle.domain.usecase.sign.SignOutUseCase
import com.aivle.domain.usecase.sign.WithdrawalUseCase
import com.aivle.domain.usecase.user.UserInfo
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MyProfileDetailViewModel @Inject constructor(
    private val getMyProfileDetailUseCase: GetMyProfileDetailUseCase,
    private val updateMyProfileDetailUseCase: UpdateMyProfileDetailUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    var userInfo: UserInfo? = null
        private set

    fun loadMyProfileDetail() = launchDefault {
        getMyProfileDetailUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    userInfo = response.data
                    _eventFlow.emit(Event.MyProfile(userInfo!!))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    fun updateMyProfile(nickname: String) = launchDefault {
        val user = userInfo?.user ?: return@launchDefault
        val updateUser = User(null, user.phoneNumber, user.name, nickname)

        updateMyProfileDetailUseCase(updateUser)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    val newUser = response.data
                    val address = userInfo!!.address
                    userInfo = UserInfo(newUser, address)

                    _eventFlow.emit(Event.UpdateProfile(newUser))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    fun signOut() = launchDefault {
        signOutUseCase()
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

    fun withdrawal() = launchDefault {
        withdrawalUseCase()
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

    sealed class Event {
        object None: Event()
        data class MyProfile(val userInfo: UserInfo) : Event()
        data class UpdateProfile(val user: User) : Event()
        object SignOut : Event()
        object Withdrawal : Event()
        data class Failure(val message: String?) : Event()
    }
}