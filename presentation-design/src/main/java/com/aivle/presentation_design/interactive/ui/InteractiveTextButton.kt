package com.aivle.presentation_design.interactive.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import com.aivle.presentation_design.R

class InteractiveTextButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        InteractiveHelper.init(this)
        background = AppCompatResources.getDrawable(context, R.drawable.bg_button)
        gravity = Gravity.CENTER
        textSize = 18f
        setTextColor(Color.WHITE)
//        setTypeface(typeface, Typeface.BOLD)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        InteractiveHelper.onTouchEvent(this, event)
        return super.onTouchEvent(event)
    }
}