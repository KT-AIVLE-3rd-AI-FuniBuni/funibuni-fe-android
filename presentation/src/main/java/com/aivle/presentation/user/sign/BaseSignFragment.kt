package com.aivle.presentation.user.sign

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.common.dpToPixels

abstract class BaseSignFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : BaseFragment<T>(layoutResId) {

    protected fun View.animateSettle(context: Context) {
        this.animate()
            .translationY(38f.dpToPixels(context))
            .alpha(1f)
            .setDuration(500L)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withStartAction { this.isVisible = true }
            .start()
    }

    protected fun View.animateFadeIn() {
        animateFadeInWithAfter(null)
    }

    protected fun View.animateFadeInWithAfter(action: (() -> Unit)?) {
        visibility = View.VISIBLE
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 0.2f, 1f)
        val translate = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -100f, 0f)

        ObjectAnimator.ofPropertyValuesHolder(this, alpha, translate).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    action?.invoke()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }.start()
    }
}