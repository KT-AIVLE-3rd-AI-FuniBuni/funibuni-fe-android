package com.aivle.presentation_design.interactive.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.aivle.presentation_design.R
import com.google.android.material.progressindicator.CircularProgressIndicator

sealed class TextButtonStatus {
    object Enabled : TextButtonStatus()
    object Loading : TextButtonStatus()
    object Disabled : TextButtonStatus()
}

class InteractiveTextButtonForSign @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var _status: TextButtonStatus = TextButtonStatus.Enabled
    var status: TextButtonStatus
        get() = _status
        set(value) { updateStatus(value) }

    var text: String
        get() = textView.text.toString()
        set(value) { textView.text = value }

    private val textView: TextView
    private val progressIndicator: CircularProgressIndicator

    init {
        isClickable = true
        background = AppCompatResources.getDrawable(context, R.drawable.bg_ripple_text_button_square_dark)

        val text = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text)).use {
            it.getString(0) ?: ""
        }
        val enabled = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.enabled)).use {
            it.getBoolean(0, true)
        }

        textView = TextView(context).also {
            it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also { params ->
                params.gravity = Gravity.CENTER
            }
            it.text = text
            it.textSize = 20f
            it.setTextColor(AppCompatResources.getColorStateList(context, R.color.selector_text_button_dark_text_color))
        }
        progressIndicator = CircularProgressIndicator(context).also {
            it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also { params ->
                params.gravity = Gravity.CENTER
            }
        }

        addView(textView)
        addView(progressIndicator)

        isEnabled = enabled
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        textView.isEnabled = enabled
//        if (enabled) {
//            updateStatus(TextButtonStatus.Enabled)
//        } else {
//            updateStatus(TextButtonStatus.Disabled)
//        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return if (!isEnabled) {
            true
        } else {
            // InteractiveHelper.onTouchEvent(this, event)
            return super.onTouchEvent(event)
        }
    }

    private fun updateStatus(status: TextButtonStatus) {
        _status = status
        when (status) {
            TextButtonStatus.Enabled -> {
                isEnabled = true
                textView.isVisible = true
                progressIndicator.isIndeterminate = false
            }
            TextButtonStatus.Loading -> {
                isEnabled = false
                textView.isVisible = false
                progressIndicator.isIndeterminate = true
            }
            TextButtonStatus.Disabled -> {
                isEnabled = false
                textView.isVisible = true
                progressIndicator.isIndeterminate = false
            }
        }
    }
}