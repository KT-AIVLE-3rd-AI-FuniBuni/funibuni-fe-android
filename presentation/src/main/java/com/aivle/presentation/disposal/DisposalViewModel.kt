package com.aivle.presentation.disposal

import androidx.lifecycle.ViewModel
import com.aivle.domain.model.waste.WasteSpec
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DisposalViewModel @Inject constructor(

) : ViewModel() {

    var wasteImageUri: String = ""

    private val _wasteSpecs: MutableList<WasteSpec> = mutableListOf()
    val wasteSpecs: List<WasteSpec> = _wasteSpecs

    var selectedWasteSpec: WasteSpec? = null

    fun updateWasteSpecs(wasteSpecs: List<WasteSpec>) {
        _wasteSpecs.clear()
        _wasteSpecs.addAll(wasteSpecs)
    }
}