package com.aivle.presentation.sharing.postDetail

import android.animation.ValueAnimator
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import com.aivle.presentation.databinding.ActivitySharingPostDetailBinding
import com.aivle.presentation.databinding.HeaderSharingPostDetailBinding
import com.google.android.material.appbar.AppBarLayout

class SharingPostDetailHeaderAdapter constructor(
    private val binding: ActivitySharingPostDetailBinding,
) : AppBarLayout.OnOffsetChangedListener {

    var isShowingTitleBar = false
        private set

    var systemBarHeight = 0
        private set

    var navigationBarHeight = 0
        private set

    private val bindingHeader: HeaderSharingPostDetailBinding
        get() = binding.header

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
        ViewCompat.setOnApplyWindowInsetsListener(bindingHeader.root) { _, windowInsets ->
            val systemBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val navigationBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            systemBarHeight = systemBarInsets.top
            navigationBarHeight = navigationBarInsets.bottom

            // 헤더를 statusBar 높이 만큼 내리기
            bindingHeader.statusBarGuideline.setGuidelineBegin(systemBarHeight)
            // 하단 마진 주기
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> { bottomMargin = systemBarInsets.bottom }

            WindowInsetsCompat.CONSUMED
        }

        initOffset()
    }

    fun onBackPressed(onClicked: () -> Unit) = apply {
        bindingHeader.btnBack.setOnClickListener { onClicked.invoke() }
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

        bindingHeader.backgroundWhite.animate().setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(150L)
            .alpha(if (isShow) 1f else 0f)
            .start()
//        bindingHeader.tvShopName.animate().setInterpolator(AccelerateDecelerateInterpolator())
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
                bindingHeader.btnBack.drawable.setTint(color)
                bindingHeader.btnTheMore.drawable.setTint(color)
            }
        }.start()

        windowInsetsControllerCompat?.isAppearanceLightStatusBars = isShow
    }

    private fun initOffset() {
        bindingHeader.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (bindingHeader.btnBack.bottom > 0) {
                    offset = bindingHeader.btnBack.bottom
                    bindingHeader.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }
}