package com.aivle.presentation.disposal.wasteclassification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.waste.ClassifyWasteImageUseCase
import com.aivle.domain.usecase.waste.GetWasteSpecTableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WasteClassificationViewModel"

@HiltViewModel
class WasteClassificationViewModel @Inject constructor(
    private val ClassifyWasteImageUseCase: ClassifyWasteImageUseCase,
    private val GetWasteSpecTableUseCase: GetWasteSpecTableUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    private val _allWasteSpecs: MutableList<WasteSpec> = mutableListOf()
    val allWasteSpecs: List<WasteSpec> get() = _allWasteSpecs

    fun classifyWasteImage(imageUri: String) {
        viewModelScope.launch {
            ClassifyWasteImageUseCase(imageUri)
                .catch { _eventFlow.emit(Event.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        val data = response.data

                        _allWasteSpecs.clear()
                        _allWasteSpecs.addAll(data.all_waste_specs)

                        _eventFlow.emit(Event.ClassificationResult(data))

                        Log.d(TAG, response.data.toString())
                    }
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    fun loadWasteSpecTable() {
        viewModelScope.launch {
            GetWasteSpecTableUseCase()
                .catch { _eventFlow.emit(Event.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        _allWasteSpecs.clear()
                        _allWasteSpecs.addAll(response.data)
                        _eventFlow.emit(Event.WasteSpecTable(response.data))
                    }
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    sealed class Event {
        object None : Event()
        data class ClassificationResult(val document: WasteClassificationDocument) : Event()
        data class WasteSpecTable(val specs: List<WasteSpec>) : Event()
        data class Failure(val message: String?) : Event()
    }
}