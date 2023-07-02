package com.aivle.presentation.sharing.postCreate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateSharingPostViewModel @Inject constructor(

) : ViewModel() {

    fun createSharingPost() {

    }

    sealed class Event {

    }
}