package com.aivle.presentation.disposal.wasteclassification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.waste.ClassifyWasteImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WasteClassificationViewModel"

@HiltViewModel
class WasteClassificationViewModel @Inject constructor(
    private val ClassifyWasteImageUseCase: ClassifyWasteImageUseCase
) : ViewModel() {

    fun classifyWasteImage(imageUri: String) {
        viewModelScope.launch {
            ClassifyWasteImageUseCase(imageUri)
                .catch { Log.d(TAG, it.message.toString()) }
                .collect { response -> when (response) {
                    is DataResponse.Success -> {
                        Log.d(TAG, response.data.toString())
                    }
                    is DataResponse.Failure -> {
                        Log.d(TAG, response.message.toString())
                    }
                }}
        }
    }
}