package com.aivle.presentation.disposal.base

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.disposal.DisposalViewModel
import java.io.IOException

abstract class BaseDisposalFragment<T : ViewDataBinding>(
    @LayoutRes layoutResId: Int
) : BaseFragment<T>(layoutResId) {

    protected val activityViewModel: DisposalViewModel by activityViewModels()

    protected val wasteImageBitmap: Bitmap?
        get() = getImageBitmapFrom(activityViewModel.wasteImageLocalUri)

    private fun getImageBitmapFrom(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}