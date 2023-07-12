package com.aivle.presentation.sharing.postList

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.sharingPost.GetSharingPostListUserCase
import com.aivle.presentation.util.common.ListUiState
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharingPostListViewModel @Inject constructor(
    private val getSharingPostListUseCase: GetSharingPostListUserCase,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<ListUiState<SharingPostItem>>(ListUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun loadSharingPosts(district: String) = launchDefault {
        getSharingPostListUseCase(district)
            .catch { t ->
                _uiStateFlow.update { it.copy(isLoading = false, message = t.message) }
            }
            .collect { response -> when (response) {
                is DataResponse.Success -> {
                    _uiStateFlow.update { it.copy(isLoading = false, message = null, data = response.data) }
                }
                is DataResponse.Failure -> {
                    _uiStateFlow.update { it.copy(isLoading = false, message = null) }
                }
            }}
    }
}