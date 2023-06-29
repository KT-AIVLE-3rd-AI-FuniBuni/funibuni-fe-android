package com.aivle.domain.response

sealed class NothingResponse {

    object Success : NothingResponse()

    sealed class Failure : NothingResponse() {
        abstract val message: String?

        data class Error(
            override val message: String?
        ) : Failure()

        data class Exception(
            val throwable: Throwable,
            override val message: String?
        ) : Failure()
    }
}