package com.aivle.presentation.util.ext

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    Log.w(this::class.simpleName, "showToast(): $message")
}

fun Fragment.showToast(message: String?) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    Log.w(this::class.simpleName, "showToast(): $message")
}