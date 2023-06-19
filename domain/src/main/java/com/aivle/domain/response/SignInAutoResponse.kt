package com.aivle.domain.response

sealed class SignInAutoResponse {

    object Success : SignInAutoResponse()

    sealed class Failure : SignInAutoResponse() {

        object ExpiredToken : Failure()
        object NotFoundUser: Failure()
    }

    data class Exception(val message: String?) : SignInAutoResponse()
}