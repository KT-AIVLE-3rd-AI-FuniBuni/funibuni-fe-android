package com.aivle.presentation.util.model

import java.util.Calendar
import java.util.concurrent.TimeUnit

data class FuniBuniDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
) {

    private val calendar = Calendar.getInstance().apply {
        set(year, month, day, hour, minute, second)
    }

    fun calculateDiffDay(year: Int, month: Int, day: Int): Int {
        val other = Calendar.getInstance().apply { set(year, month, day, 23, 59, 59) }
        val diffInMillis = other.timeInMillis - calendar.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    }

    operator fun minus(other: FuniBuniDate): Int {
        // 일 수 차이 계산
        val temp = Calendar.getInstance().apply {
            timeInMillis = calendar.timeInMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        val diffInMillis = temp.timeInMillis - other.calendar.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    }

    operator fun plus(day: Int): FuniBuniDate {
        val instance = getInstance(calendar.timeInMillis).also {
            it.calendar.add(Calendar.DAY_OF_YEAR, day)
        }
        return getInstance(instance.calendar.timeInMillis)
    }

    override fun toString(): String {
        return "${year}년 ${month + 1}일 ${day}일 ${hour}시 ${minute}분 ${second}초"
    }

    fun toDateString(): String {
        return "${year}년 ${month + 1}월 ${day}일"
    }

    fun toTimeString(): String {
        return "${hour}시 ${minute}분 ${second}초"
    }

    fun toServerFormat(): String {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d+09:00", year, month+1, day, 23, 59, 59)
    }

    companion object {

        fun today(): FuniBuniDate = getInstance(Calendar.getInstance().timeInMillis)

        fun getInstance(timeInMillis: Long): FuniBuniDate {
            val newCalendar = Calendar.getInstance()
            newCalendar.timeInMillis = timeInMillis
            return FuniBuniDate(
                newCalendar[Calendar.YEAR], newCalendar[Calendar.MONTH], newCalendar[Calendar.DAY_OF_MONTH],
                newCalendar[Calendar.HOUR_OF_DAY], newCalendar[Calendar.MINUTE], newCalendar[Calendar.SECOND],
            )
        }

        fun calculatePeriod(year: Int, month: Int, day: Int): Int {
            val todayCalendar = Calendar.getInstance()
            val otherCalendar = Calendar.getInstance()
            otherCalendar.set(year, month, day) // 비교할 날짜를 설정합니다.

            val diffInMillis = otherCalendar.timeInMillis - todayCalendar.timeInMillis
            return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
        }
    }
}