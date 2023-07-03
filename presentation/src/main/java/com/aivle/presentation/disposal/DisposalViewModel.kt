package com.aivle.presentation.disposal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.address.Address
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.domain.usecase.address.GetAddressFromLocalUseCase
import com.aivle.presentation.util.ext.launchDefault
import com.aivle.presentation.util.model.ClassificationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DisposalViewModel"

@HiltViewModel
class DisposalViewModel @Inject constructor(
    private val getAddressFromLocalUseCase: GetAddressFromLocalUseCase,
) : ViewModel() {

    private val _eventFlow: MutableSharedFlow<Event> = MutableSharedFlow()
    val eventFlow: SharedFlow<Event> get() = _eventFlow

    var wasteImageLocalUri: String = ""

    private val _wasteSpecTable: MutableList<WasteSpec> = mutableListOf()
    val wasteSpecTable: List<WasteSpec> = _wasteSpecTable

    // var document: WasteClassificationDocument? = null
    var classificationResult: ClassificationResult? = null
    var selectedWasteSpec: WasteSpec? = null
    var address: Address? = null

    fun loadAddress() = launchDefault {
        address = getAddressFromLocalUseCase()
    }

    fun updateWasteSpecs(wasteSpecs: List<WasteSpec>) {
        _wasteSpecTable.clear()
        _wasteSpecTable.addAll(wasteSpecs)
    }

    fun completeApplication(wasteApplyId: Int) {
        viewModelScope.launch {
            _eventFlow.emit(Event.ApplyCompleted(wasteApplyId))
        }
    }

    sealed class Event {
        data class ApplyCompleted(val wasteApplyId: Int) : Event()
    }
}