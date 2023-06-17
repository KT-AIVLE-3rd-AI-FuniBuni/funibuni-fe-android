package com.aivle.domain.model.user

sealed class SignInResponse {

    object Success : SignInResponse()

    sealed class Error : SignInResponse() {
        object NotExistsUser : Error()
    }

    data class Exception(val message: String?) : SignInResponse()
}
