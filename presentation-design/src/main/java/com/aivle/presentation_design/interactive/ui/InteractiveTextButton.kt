package com.aivle.presentation_design.interactive.ui

import android.content.Context
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
        isClickable = true

        context.obtainStyledAttributes(attrs, R.styleable.InteractiveTextButton).use { typedArray ->
            val type = typedArray.getInteger(R.styleable.InteractiveTextButton_buttonType, 0)
            when (type) {
                0 -> { // type: dark
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_ripple_text_button_dark)
                    setTextColor(AppCompatResources.getColorStateList(context, R.color.selector_text_button_dark_text_color))
                }
                1 -> { // type: light
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_ripple_text_button_light)
                    setTextColor(AppCompatResources.getColorStateList(context, R.color.selector_text_button_light_text_color))
                }
                else -> throw IllegalArgumentException("type=$type")
            }

            gravity = Gravity.CENTER
            textSize = 20f
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return if (!isEnabled) {
            true
        } else {
            InteractiveHelper.onTouchEvent(this, event)
            return super.onTouchEvent(event)
        }
    }
}