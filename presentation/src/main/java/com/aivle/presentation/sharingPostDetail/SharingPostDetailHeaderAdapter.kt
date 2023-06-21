package com.aivle.presentation.sharingPostDetail

import android.animation.ValueAnimator
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.*
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aivle.presentation.databinding.HeaderSharingPostDetailBinding
import com.google.android.material.appbar.AppBarLayout

class SharingPostDetailHeaderAdapter constructor(
    private val binding: HeaderSharingPostDetailBinding,
) : AppBarLayout.OnOffsetChangedListener {

    var isShowingTitleBar = false
        private set

    private var offset = 0
    private var userOffset = 0

    private var windowInsetsControllerCompat: WindowInsetsControllerCompat? = null


    fun init(window: Window, appBarLayout: AppBarLayout) = apply {
        windowInsetsControllerCompat = ViewCompat.getWindowInsetsController(window.decorView)

        // add AppBar Scroll Offset
        appBarLayout.addOnOffsetChangedListener(this)

        // Window Settings
        window.statusBarColor = Color.TRANSPARENT

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.statusBarGuideline.setGuidelineBegin(insets.top)
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> { bottomMargin = insets.bottom }

            WindowInsetsCompat.CONSUMED
        }

        initOffset()
    }

    fun onBackPressed(onClicked: () -> Unit) = apply {
        binding.btnBack.setOnClickListener { onClicked.invoke() }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val endedAppBarScrollOffset = (appBarLayout.totalScrollRange - (offset + userOffset) <= -verticalOffset)
        when {
            endedAppBarScrollOffset && isShowingTitleBar.not() -> animateTitleBar(true)
            endedAppBarScrollOffset.not() && isShowingTitleBar -> animateTitleBar(false)
        }
    }

    private fun animateTitleBar(isShow: Boolean) {
        isShowingTitleBar = isShow

        binding.backgroundWhite.animate().setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(150L)
            .alpha(if (isShow) 1f else 0f)
            .start()
//        binding.tvShopName.animate().setInterpolator(AccelerateDecelerateInterpolator())
//            .setDuration(150L)
//            .alpha(if (isShow) 1f else 0f)
//            .start()

        val colors =
            if (isShow) Color.WHITE to Color.BLACK
            else Color.BLACK to Color.WHITE

        ValueAnimator.ofArgb(colors.first, colors.second).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 150L
            addUpdateListener {
                val color = it.animatedValue as Int
                binding.btnBack.drawable.setTint(color)
                binding.btnTheMore.drawable.setTint(color)
            }
        }.start()

        windowInsetsControllerCompat?.isAppearanceLightStatusBars = isShow
    }

    private fun initOffset() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding.btnBack.bottom > 0) {
                    offset = binding.btnBack.bottom
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }
}