package com.aivle.presentation.disposal.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.address.Address
import com.aivle.domain.model.waste.WasteDisposalApply
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.address.GetAddressFromLocalUseCase
import com.aivle.domain.usecase.waste.ApplyWasteDisposalUseCase
import com.aivle.presentation.util.model.ClassificationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WasteDisposalApplyViewModel @Inject constructor(
    private val getAddressFromLocalUseCase: GetAddressFromLocalUseCase,
    private val applyWasteDisposalUseCase: ApplyWasteDisposalUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    private var _address: Address? = null

    fun loadAddress() {
        viewModelScope.launch {
            val address = getAddressFromLocalUseCase()
            _address = address
            _eventFlow.emit(Event.AddressLoaded(address))
        }
    }

    fun applyWasteDisposal(
        classificationResult: ClassificationResult,
        wasteSpec: WasteSpec,
        datetime: String,
        disposalLocation: String,
        memo: String,
    ) {
        val address = this._address ?: return

        viewModelScope.launch {
            val applyFormat = WasteDisposalApply(
                classificationResult.wasteId,
                wasteSpec.waste_spec_id,
                classificationResult.imageTitle,
                classificationResult.imageUrl,
                address.postalCode,
                address.landAddress,
                address.roadAddress,
                address.city,
                address.district,
                address.dong,
                address.detail,
                disposalLocation,
                datetime,
                memo,
            )
            applyWasteDisposalUseCase(applyFormat)
                .catch {
                    _eventFlow.emit(Event.Apply.Failure(it.message))
                }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        val wasteApplyId = response.data.waste_id
                        _eventFlow.emit(Event.Apply.Success(wasteApplyId))
                    }
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Apply.Failure(response.message))
                    }
                }}
        }
    }

    sealed class Event {
        object None: Event()
        data class AddressLoaded(val address: Address) : Event()
        sealed class Apply : Event() {
            data class Success(val applyId: Int) : Apply()
            data class Failure(val message: String?) : Apply()
        }
    }
}