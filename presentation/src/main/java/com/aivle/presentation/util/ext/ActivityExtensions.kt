package com.aivle.presentation.util.ext

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    // Toast.makeText(this, message, duration).show()
    Log.w(this::class.simpleName, "showToast(): $message")
}

fun Fragment.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    // Toast.makeText(requireContext(), message, duration).show()
    Log.w(this::class.simpleName, "showToast(): $message")
}