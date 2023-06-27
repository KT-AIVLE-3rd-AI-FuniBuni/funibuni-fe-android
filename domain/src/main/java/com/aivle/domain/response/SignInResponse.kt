package com.aivle.domain.response

sealed class SignInResponse {

    object Success : SignInResponse()

    sealed class Error : SignInResponse() {
        object NotFoundUser : Error()
    }

    data class Exception(val message: String?) : SignInResponse()
}
