//package com.aivle.presentation.util.model
//
//import java.util.Calendar
//import java.util.concurrent.TimeUnit
//
//data class FuniBuniDate(
//    val year: Int,
//    val month: Int,
//    val day: Int,
//    val hour: Int = 0,
//    val minute: Int = 0,
//    val second: Int = 0,
//) {
//
//
//    private val calendar = Calendar.getInstance().apply {
//        set(year, month, day, hour, minute, second)
//    }
//
//    operator fun minus(other: FuniBuniDate): FuniBuniDate {
//        val diffMillis = calendar.timeInMillis - other.calendar.timeInMillis
//        val calendar.timeInMillis = diffMillis
//        return TimeUnit.MILLISECONDS.toDays(diffMillis)
//    }
//
//    companion object {
//
//        fun today(): FuniBuniDate = Calendar.getInstance().let {
//            FuniBuniDate(it[Calendar.YEAR], it[Calendar.MONTH], it[Calendar.DAY_OF_MONTH])
//        }
//
//        fun getInstance(timeInMillis: Long): FuniBuniDate {
//            val newCalendar = Calendar.getInstance()
//            newCalendar.timeInMillis = timeInMillis
//            return FuniBuniDate(
//
//            )
//        }
//
//        fun calculatePeriod(year: Int, month: Int, day: Int): Int {
//            val todayCalendar = Calendar.getInstance()
//            val otherCalendar = Calendar.getInstance()
//            otherCalendar.set(year, month, day) // 비교할 날짜를 설정합니다.
//
//            val diffInMillis = otherCalendar.timeInMillis - todayCalendar.timeInMillis
//            return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
//        }
//    }
//}
