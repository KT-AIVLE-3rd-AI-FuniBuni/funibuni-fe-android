package com.aivle.presentation.myprofile.applyList

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.myProfile.GetWasteDisposalApplyListUseCase
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class WasteDisposalApplyListViewModel @Inject constructor(
    private val getWasteDisposalApplyListUseCase: GetWasteDisposalApplyListUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.None)
    val eventFlow: StateFlow<Event> get() = _eventFlow

    fun loadWasteDisposalApplies() = launchDefault {
        getWasteDisposalApplyListUseCase()
            .catch { _eventFlow.emit(Event.Failure(it.message)) }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    _eventFlow.emit(Event.LoadApplyList.Success(response.data))
                }
                is DataResponse.Failure -> {
                    _eventFlow.emit(Event.Failure(response.message))
                }
            }}
    }

    sealed class Event {
        object None : Event()
        sealed class LoadApplyList : Event() {
            data class Success(val applies: List<WasteDisposalApplyItem>) : LoadApplyList()
        }
        data class Failure(val message: String?) : Event()
    }
}