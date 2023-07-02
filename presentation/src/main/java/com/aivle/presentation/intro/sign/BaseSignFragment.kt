package com.aivle.presentation.intro.sign

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.util.ext.dpToPixels
import com.aivle.presentation.util.common.KeyboardHeightProvider

private const val TAG = "BaseSignFragment"

abstract class BaseSignFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : BaseFragment<T>(layoutResId) {

    abstract val bottomButtonGuideLine: Guideline?

    protected val signActivity: SignActivity
        get() = requireActivity() as SignActivity

    protected val signViewModel: SignViewModel by activityViewModels()

    protected val isShowingKeyboard: Boolean
        get() = signActivity.keyboardHeightProvider.isShowingKeyboard

    private val keyboardListener = object : KeyboardHeightProvider.OnKeyboardListener {
        override fun onHeightChanged(height: Int, isShowing: Boolean) {
            if (isShowing) {
                bottomButtonGuideLine?.setGuidelineEnd(height)
            } else {
                bottomButtonGuideLine?.setGuidelineEnd(0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        signActivity.keyboardHeightProvider.addOnKeyboardListener(keyboardListener)
    }

    override fun onResume() {
        super.onResume()
        signActivity.keyboardHeightProvider.addOnKeyboardListener(keyboardListener)
    }

    override fun onPause() {
        super.onPause()
        signActivity.keyboardHeightProvider.removeOnKeyboardListener(keyboardListener)
    }

    override fun onDestroy() {
        super.onDestroy()
//        signActivity.keyboardHeightProvider.removeOnKeyboardListener(keyboardListener)
    }

    protected fun hideKeyboard() {
        signActivity.keyboardHeightProvider.hideKeyboard()
    }

    protected fun moveNextPage() {
        (requireActivity() as SignActivity).moveNextPage()
    }

    protected fun startSmsUserConsent(phoneNumber: String) {
        (requireActivity() as SignActivity).startSmsUserConsent(phoneNumber)
    }

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