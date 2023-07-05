package com.aivle.presentation.disposal.wasteclassification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.waste.ClassifyWasteImageUseCase
import com.aivle.domain.usecase.waste.GetWasteSpecTableUseCase
import com.aivle.presentation.util.model.AiResult
import com.aivle.presentation.util.model.AiResultGroup
import com.aivle.presentation.util.model.ClassificationResult
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

    private val _wasteSpecTable: MutableList<WasteSpec> = mutableListOf()
    val wasteSpecTable: List<WasteSpec> get() = _wasteSpecTable

    fun classifyWasteImage(imageUri: String) {
        viewModelScope.launch {
            _eventFlow.emit(Event.ImageClassification.Loading)

            ClassifyWasteImageUseCase(imageUri)
                .catch { _eventFlow.emit(Event.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        val data = response.data
                        _wasteSpecTable.clear()
                        _wasteSpecTable.addAll(data.all_waste_specs)

                        transform(data)
                    }
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    private suspend fun transform(document: WasteClassificationDocument) {
        Log.d(TAG, "foo(): $document")
        val wasteSpecTable = document.all_waste_specs
        val labels = document.labels
        if (labels.isEmpty()) {
            _eventFlow.emit(Event.ImageClassification.Empty)
            return
        }

        val groups = labels.map { label ->
            val (indexLarge, nameLarge, probLarge) = label.large_category
            val wasteSpecs = wasteSpecTable.filter { it.index_large_category == indexLarge }
            val indexSmall = label.small_category?.index_small_category ?: -100

            val largeResult = AiResult(
                index = indexLarge,
                categoryName = nameLarge,
                probability = probLarge,
                spec = wasteSpecs.first()
            )
            val smallResults = wasteSpecs.map {
                val probSmall = if (it.index_small_category == indexSmall) {
                    label.small_category!!.probability
                } else {
                    0f
                }
                AiResult(it.index_small_category, it.small_category, probSmall, it)
            }
            AiResultGroup(largeResult, smallResults)
        }
        val result = ClassificationResult(
            document.waste_id,
            document.image_title,
            document.image_url,
            groups,
        )
        _eventFlow.emit(Event.ImageClassification.Result(result))
    }

    fun loadWasteSpecTable() {
        viewModelScope.launch {
            _eventFlow.emit(Event.ImageClassification.Loading)

            GetWasteSpecTableUseCase()
                .catch { _eventFlow.emit(Event.Failure(it.message)) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        _wasteSpecTable.clear()
                        _wasteSpecTable.addAll(response.data)
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

        sealed class ImageClassification : Event() {
            object Loading: Event()
            object Empty : ImageClassification()
            data class Result(val result: ClassificationResult) : ImageClassification()

        }
        data class WasteSpecTable(val specs: List<WasteSpec>) : Event()
        data class Failure(val message: String?) : Event()
    }
}