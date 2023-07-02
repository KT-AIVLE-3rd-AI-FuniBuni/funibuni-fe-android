package com.aivle.presentation.disposal.applyDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.waste.WasteDisposalApplyDetail
import com.aivle.domain.repository.WasteRepository
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.waste.CancelWasteDisposalApplyUseCase
import com.aivle.domain.usecase.waste.GetWasteDisposalApplyDetailUseCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WasteDisposalApplyDetailViewModel"

@HiltViewModel
class WasteDisposalApplyDetailViewModel @Inject constructor(
    private val getWasteDisposalApplyDetailUseCase: GetWasteDisposalApplyDetailUseCase,
    private val cancelWasteDisposalApplyUseCase: CancelWasteDisposalApplyUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadApplyDetail(wasteApplyId: Int) = launchDefault {
        getWasteDisposalApplyDetailUseCase(wasteApplyId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    Log.d(TAG, "loadApplyDetail(DataResponse.Success): ${response.data}")
                    _eventFlow.emit(Event.LoadApplyDetail.Success(response.data))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    fun cancelWasteDisposalApply(wasteApplyId: Int) = launchDefault {
        cancelWasteDisposalApplyUseCase(wasteApplyId)
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is NothingResponse.Success -> {
                    _eventFlow.emit(Event.CancelApply.Success)
                }
                is NothingResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class LoadApplyDetail : Event() {
            data class Success(val detail: WasteDisposalApplyDetail) : LoadApplyDetail()
        }
        sealed class CancelApply : Event() {
            object Success : CancelApply()
        }
        data class Failure(val message: String?) : Event()
    }
}