package com.aivle.presentation.myprofile.tab

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.mybuni.MyBuni
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.myProfile.GetMyBuniUseCase
import com.aivle.domain.usecase.myProfile.GetMyProfileDetailUseCase
import com.aivle.domain.usecase.user.UserInfo
import com.aivle.presentation.util.common.ListUiState
import com.aivle.presentation.util.common.UiState
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getMyBuniUseCase: GetMyBuniUseCase,
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<UiState<MyBuni>> = MutableStateFlow(UiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun loadMyBuni() = launchDefault {
        getMyBuniUseCase()
            .catch {
                _uiStateFlow.update { value -> value.copy(isLoading = false, message = it.message) }
            }
            .collect { response -> when (response) {
                is DataResponse.Failure -> {
                    _uiStateFlow.update { it.copy(isLoading = false, message = response.message) }
                }
                is DataResponse.Success -> {
                    _uiStateFlow.update { it.copy(isLoading = false, message = null, data = response.data) }
                }
            }}
    }
}