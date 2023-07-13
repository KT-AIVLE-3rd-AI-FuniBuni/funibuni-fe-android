package com.loggi.core_util.extensions

import android.util.Log

private const val DEBUG = true

fun Any.log(message: String?, forced: Boolean = false) {
    if (DEBUG || forced) {
        Log.d(javaClass.simpleName, message.toString())
    }
}

fun Any.logd(message: String?, forced: Boolean = false) {
    if (DEBUG || forced) {
        Log.d(javaClass.simpleName, message.toString())
    }
}

fun Any.logi(message: String?, forced: Boolean = false) {
    if (DEBUG || forced) {
        Log.i(javaClass.simpleName, message.toString())
    }
}

fun Any.logw(message: String?, forced: Boolean = false) {
    if (DEBUG || forced) {
        Log.w(javaClass.simpleName, message.toString())
    }
}

fun Any.loge(message: String?, forced: Boolean = false) {
    if (DEBUG || forced) {
        Log.e(javaClass.simpleName, message.toString())
    }
}