package com.aivle.presentation._util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.IOException


object BitmapUtil {

    fun decodeFile(path: String): Bitmap? {
        val bitmapOrigin = getImageBitmap(path) ?: return null
        val angle = getRotateAngle(path)
        return rotateBitmap(bitmapOrigin, angle)
    }

    private fun getImageBitmap(path: String): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }

    private fun getRotateAngle(path: String): Float {
        val exif = ExifInterface(path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationAngle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotationAngle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}