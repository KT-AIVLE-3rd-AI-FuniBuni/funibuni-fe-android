package com.aivle.presentation.searchWasteSpec

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.waste.WasteSpec
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.waste.GetWasteSpecTableUseCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class SearchWasteSpecViewModel @Inject constructor(
    private val getWasteSpecTableUseCase: GetWasteSpecTableUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    private var wasteSpecTable: List<WasteSpec>? = null

    fun loadWasteSpecTable() = launchDefault {
        getWasteSpecTableUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    wasteSpecTable = response.data
                    _eventFlow.emit(Event.LoadedWasteSpecTable(wasteSpecTable!!))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            } }
    }

    fun search(keyword: String) = launchDefault {
        val table = wasteSpecTable ?: return@launchDefault

        if (keyword.isBlank()) {
            _eventFlow.emit(Event.SearchWasteSpecResult(table))
            return@launchDefault
        }

        val results = table.filter {
            it.top_category.contains(keyword) ||
            it.large_category.contains(keyword) ||
            it.small_category.contains(keyword)
        }

        _eventFlow.emit(Event.SearchWasteSpecResult(results))
    }


    sealed class Event {
        object None : Event()
        data class LoadedWasteSpecTable(val table: List<WasteSpec>) : Event()
        data class SearchWasteSpecResult(val results: List<WasteSpec>) : Event()
        data class Failure(val message: String?) : Event()
    }
}