package com.aivle.presentation.myprofile.applyList

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.waste.WasteDisposalApplyItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.myProfile.GetWasteDisposalApplyListUseCase
import com.aivle.presentation.util.common.ListUiState
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WasteDisposalApplyListViewModel @Inject constructor(
    private val getWasteDisposalApplyListUseCase: GetWasteDisposalApplyListUseCase,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<ListUiState<WasteDisposalApplyItem>>(ListUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun loadWasteDisposalApplies() = launchDefault {
        getWasteDisposalApplyListUseCase()
            .catch {
                _uiStateFlow.update { value -> value.copy(isLoading = false, message = it.message) }
            }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _uiStateFlow.update { it.copy(isLoading = false, message = response.message) }
                }
                is DataResponse.Success -> {
                    _uiStateFlow.update { it.copy(isLoading = false, message = null, response.data) }
                }
            }}
    }
}