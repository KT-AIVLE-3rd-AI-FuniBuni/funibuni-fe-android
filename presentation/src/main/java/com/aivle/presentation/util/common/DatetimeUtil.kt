package com.aivle.presentation.util.common

import java.text.SimpleDateFormat
import java.util.Locale

object DatetimeUtil {

    fun dateString(datetime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(datetime)
    }

    fun toServer(dateString: String, hour: Int, minute: Int, second: Int): String {
        val timeString = String.format("%02d:%02d:%02d", hour, minute, second)
        return "${dateString}T${timeString}+09:00"
    }

    fun toApp(datetime: String): String {
        return ""
    }
}