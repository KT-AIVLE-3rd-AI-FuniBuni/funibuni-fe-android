package com.aivle.domain.response

import kotlin.Nothing

sealed class DataResponse<out T> {

    data class Success<out T>(val data: T) : DataResponse<T>()

    sealed class Failure : DataResponse<Nothing>() {

        abstract val message: String?

        data class Error(override val message: String?): Failure()

        data class Exception(
            override val message: String?,
            val throwable: Throwable,
        ) : Failure()
    }
}