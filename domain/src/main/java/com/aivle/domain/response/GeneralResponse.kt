package com.aivle.domain.response

import kotlin.Nothing

sealed class GeneralResponse<out T> {

    data class Success<out T>(val data: T) : GeneralResponse<T>()

    data class Failure(val message: String?): GeneralResponse<Nothing>()

    data class Exception(
        val throwable: Throwable,
        val message: String?
    ) : GeneralResponse<Nothing>()
}