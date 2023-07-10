package com.aivle.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.address.Address
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.address.GetAddressFromLocalUseCase
import com.aivle.domain.usecase.address.GetAddressListFromRemoteUseCase
import com.aivle.domain.usecase.address.SetAddressFromLocalUseCase
import com.aivle.domain.usecase.user.GetUserUseCase
import com.aivle.presentation.util.common.Constants
import com.aivle.presentation.util.ext.launchDefault
import com.loggi.core_util.extensions.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAddressFromLocalUseCase: GetAddressFromLocalUseCase,
    private val setAddressFromLocalUseCase: SetAddressFromLocalUseCase,
    private val getAddressListFromRemoteUseCase: GetAddressListFromRemoteUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    var address: Address? = null
        private set

    fun loadAddress() {
        viewModelScope.launch {
            address = getAddressFromLocalUseCase()
            log("loadAddress(): address=$address")

            // 회원가입 한 뒤 앱 삭제 or 앱 데이터 삭제 후 로그인을 하면 주소가 날아감
            if (address?.roadAddress.isNullOrBlank()) {
                getAddressListFromRemoteUseCase()
                    .collect {
                        if (it is DataResponse.Success) {
                            address = it.data.first()
                            setAddressFromLocalUseCase(address!!)

                            _eventFlow.emit(Event.AddressLoaded(address!!))
                        }
                    }
            } else {
                _eventFlow.emit(Event.AddressLoaded(address!!))
            }
        }
    }

    fun loadUserInfo() = launchDefault {
        getUserUseCase()
            .collect {
                if (it is DataResponse.Success) {
                    Constants.userId = it.data.user.id ?: -1
                }
            }
    }

    sealed class Event {
        object None : Event()
        data class AddressLoaded(val address: Address) : Event()
    }
}