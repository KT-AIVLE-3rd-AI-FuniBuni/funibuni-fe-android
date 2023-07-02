package com.aivle.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.address.Address
import com.aivle.domain.usecase.address.GetAddressFromLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val GetAddressFromLocalUseCase: GetAddressFromLocalUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadAddress() {
        viewModelScope.launch {
            val address = GetAddressFromLocalUseCase()
            _eventFlow.emit(Event.AddressLoaded(address))
        }
    }

    sealed class Event {
        object None : Event()
        data class AddressLoaded(val address: Address) : Event()
    }
}