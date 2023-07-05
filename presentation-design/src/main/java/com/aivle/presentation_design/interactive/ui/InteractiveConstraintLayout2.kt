package com.aivle.presentation_design.interactive.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.aivle.presentation_design.R

class InteractiveConstraintLayout2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        isClickable = true
        foreground = AppCompatResources.getDrawable(context, R.drawable.bg_ripple_white)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        InteractiveHelper.onTouchEvent(this, event)
        return super.onTouchEvent(event)
    }

    fun scaleUpForced() {
        this.animate()
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(100)
            .scaleX(1f)
            .scaleY(1f)
            .start()
    }
}