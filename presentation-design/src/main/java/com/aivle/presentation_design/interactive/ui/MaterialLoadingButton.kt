package com.aivle.presentation_design.interactive.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class MaterialLoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val _isLoading: Boolean = false
    var isLoading: Boolean
        get() = _isLoading
        set(value) { updateState(value) }

    private val button: MaterialButton

    init {
        button = MaterialButton(context)


        addView(button)
    }

    private fun updateState(isLoading: Boolean) {

    }
}