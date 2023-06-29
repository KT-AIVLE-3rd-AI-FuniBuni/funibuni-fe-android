package com.aivle.presentation.disposal

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DisposalViewModel @Inject constructor(

) : ViewModel() {

    var wasteImageUri: String = ""
}