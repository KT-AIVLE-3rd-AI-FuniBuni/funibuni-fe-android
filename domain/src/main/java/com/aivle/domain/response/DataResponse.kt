package com.aivle.domain.response

import kotlin.Nothing

sealed class DataResponse<out T> {

    data class Success<out T>(val data: T) : DataResponse<T>()

    data class Failure(val message: String?): DataResponse<Nothing>()

    data class Exception(
        val throwable: Throwable,
        val message: String?
    ) : DataResponse<Nothing>()
}