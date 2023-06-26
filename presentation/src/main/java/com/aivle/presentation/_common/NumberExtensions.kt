package com.aivle.presentation._common

import android.content.Context
import android.util.DisplayMetrics

fun Int.dpToPixels(context: Context): Int =
    (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

fun Int.pixelsToDp(context: Context): Int =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

fun Float.dpToPixels(context: Context): Float =
    (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

fun Float.pixelsToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))