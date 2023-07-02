package com.aivle.presentation.util.ext

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.pow

fun Int.dpToPixels(context: Context): Int =
    (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

fun Int.pixelsToDp(context: Context): Int =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

fun Float.dpToPixels(context: Context): Float =
    (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

fun Float.pixelsToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

fun Int.pow(n: Int): Int = this.toDouble().pow(n.toDouble()).toInt()

fun Float.round(n: Int): Float = (this * 10.pow(n)).toInt() * 0.1.pow(n.toDouble()).toFloat()