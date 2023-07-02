package com.aivle.domain.model.util

import java.text.SimpleDateFormat
import java.util.Locale

object DatetimeUtil {

    fun formatDatetimeFullString(datetimeString: String): String? {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            val date = inputFormat.parse(datetimeString)

            val outputFormat = SimpleDateFormat("yyyy년 M월 d일 a h시 m분", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun formatDatetimeFullStringTwoLines(datetimeString: String): String? {
        return try {
            val (dateString, timeString) = datetimeString.split("T")
            val inputFormat1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val inputFormat2 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date = inputFormat1.parse(dateString)
            val time = inputFormat2.parse(timeString)

            val outputFormat1 = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())
            val outputFormat2 = SimpleDateFormat("a h시 m분", Locale.getDefault())
            "${outputFormat1.format(date)}\n${outputFormat2.format(time)}"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}