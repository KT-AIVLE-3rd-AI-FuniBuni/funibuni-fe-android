package com.aivle.presentation.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.Address
import com.aivle.domain.usecase.address.GetAddressUseCase
import com.aivle.domain.usecase.address.SetAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val GetAddressUseCase: GetAddressUseCase,
    private val SetAddressUseCase: SetAddressUseCase,
) : ViewModel() {

    private val _isRegisteredAddress = MutableLiveData(false)
    val isRegisteredAddress: LiveData<Boolean>
        get() = _isRegisteredAddress

    fun loadAddress() {
        viewModelScope.launch {
            _isRegisteredAddress.value = GetAddressUseCase() != null
        }
    }

    fun setAddress(value: String) {
        viewModelScope.launch {
            val address = Address(value)
            SetAddressUseCase(address)
            _isRegisteredAddress.value = true
        }
    }
}