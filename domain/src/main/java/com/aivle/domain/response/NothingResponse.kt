package com.aivle.domain.response

sealed class NothingResponse {

    object Success : NothingResponse()

    data class Failure(val message: String?): NothingResponse()

    data class Exception(
        val throwable: Throwable,
        val message: String?
    ) : NothingResponse()
}