package com.aivle.presentation_design.interactive.ui

import android.annotation.SuppressLint
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
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_text_button)
                    setTextColor(AppCompatResources.getColorStateList(context, R.color.selector_text_button_dark_text))

                }
                1 -> { // type: light
                    background = AppCompatResources.getDrawable(context, R.drawable.bg_text_button_light)
                    setTextColor(context.getColor(R.color.theme_color_dark))
                }
                else -> throw IllegalArgumentException("type=$type")
            }

            gravity = Gravity.CENTER
            textSize = 20f
            // setTypeface(typeface, Typeface.BOLD)
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

    private fun updateView(isEnabled: Boolean) {

    }
}