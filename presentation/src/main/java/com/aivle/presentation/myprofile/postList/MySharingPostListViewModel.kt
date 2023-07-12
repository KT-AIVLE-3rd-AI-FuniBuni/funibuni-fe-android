package com.aivle.presentation.myprofile.postList

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.myProfile.GetMyActivityListUseCase
import com.aivle.domain.usecase.myProfile.GetMyFavoritePostListUseCase
import com.aivle.domain.usecase.myProfile.GetMySharingPostListUseCase
import com.aivle.presentation.util.common.ListUiState
import com.aivle.presentation.util.ext.launchDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MySharingPostListViewModel @Inject constructor(
    private val getMySharingPostListUseCase: GetMySharingPostListUseCase,
    private val getMyFavoritePostListUseCase: GetMyFavoritePostListUseCase,
    private val getMyActivityListUseCase: GetMyActivityListUseCase,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<ListUiState<SharingPostItem>>(ListUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun loadMySharingPosts() = launchDefault {
        getMySharingPostListUseCase().handle()
    }

    fun loadMyFavoritePosts() = launchDefault {
        getMyFavoritePostListUseCase().handle()

    }

    fun loadMyActivities() = launchDefault {
        getMyActivityListUseCase().handle()
    }

    private suspend fun Flow<DataResponse<List<SharingPostItem>>>.handle() {
        catch {
            _uiStateFlow.update { value -> value.copy(isLoading = false, message = it.message) }
        }.collect { response ->  when (response) {
            is DataResponse.Failure -> {
                _uiStateFlow.update { it.copy(isLoading = false, message = response.message) }
            }
            is DataResponse.Success -> {
                _uiStateFlow.update { it.copy(isLoading = false, message = null, data = response.data) }
            }
        }}
    }
}