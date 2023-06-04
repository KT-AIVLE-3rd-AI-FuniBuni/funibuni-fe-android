package com.aivle.presentation.address

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.Address
import com.aivle.domain.usecase.GetAddressUseCase
import com.aivle.domain.usecase.SetAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val GetAddressUseCase: GetAddressUseCase,
    private val SetAddressUseCase: SetAddressUseCase,
) : ViewModel() {

    fun getAddress() {
        viewModelScope.launch {
            val address = GetAddressUseCase()
            Log.d("vm", "address=$address")
        }
    }

    fun setAddress(value: String) {
        viewModelScope.launch {
            SetAddressUseCase(Address(value))
        }
    }
}