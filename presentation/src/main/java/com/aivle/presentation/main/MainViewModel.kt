package com.aivle.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.address.Address
import com.aivle.domain.usecase.address.GetAddressFromLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val GetAddressFromLocalUseCase: GetAddressFromLocalUseCase,
) : ViewModel() {

    private val _eventFlow: MutableSharedFlow<Event> = MutableSharedFlow()
    val eventFlow: SharedFlow<Event> get() = _eventFlow

    fun loadAddress() {
        viewModelScope.launch {
            val address = GetAddressFromLocalUseCase()
            _eventFlow.emit(Event.AddressLoaded(address))
        }
    }

    sealed class Event {
        data class AddressLoaded(val address: Address) : Event()
    }
}