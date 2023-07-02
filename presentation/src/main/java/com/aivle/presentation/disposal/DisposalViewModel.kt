package com.aivle.presentation.disposal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteSpec
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DisposalViewModel"

@HiltViewModel
class DisposalViewModel @Inject constructor(
) : ViewModel() {

    private val _eventFlow: MutableSharedFlow<Event> = MutableSharedFlow()
    val eventFlow: SharedFlow<Event> get() = _eventFlow

    var wasteImageLocalUri: String = ""

    private val _wasteSpecTable: MutableList<WasteSpec> = mutableListOf()
    val wasteSpecTable: List<WasteSpec> = _wasteSpecTable

    var classificationResult: WasteClassificationDocument? = null
    var selectedWasteSpec: WasteSpec? = null

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