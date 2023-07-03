package com.aivle.presentation_design.interactive.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ViewAnimator
import androidx.appcompat.content.res.AppCompatResources
import com.aivle.presentation_design.R

object InteractiveHelper {

    private const val TAG = "UiHelper"

    fun init(view: View) {
        view.isClickable = true
        view.background = AppCompatResources.getDrawable(view.context, R.drawable.bg_ripple_white)
    }

    fun onTouchEvent(view: View, event: MotionEvent?) {
        Log.d(TAG, "onTouchEvent(): $event")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> animateScale(view, true)
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> animateScale(view, false)
        }
    }

    private fun animateScale(view: View, down: Boolean) {
        Log.d(TAG, "animateScale(down=$down)")
        view.animate()
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(100)
            .scaleX(if (down) 0.94f else 1f)
            .scaleY(if (down) 0.94f else 1f)
            .start()
    }
}