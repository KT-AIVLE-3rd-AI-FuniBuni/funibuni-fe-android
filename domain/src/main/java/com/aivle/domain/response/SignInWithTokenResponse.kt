package com.aivle.domain.response

sealed class SignInWithTokenResponse {

    object Success : SignInWithTokenResponse()

    sealed class Failure : SignInWithTokenResponse() {
        object NotFoundUser: Failure()
        object ExpiredToken : Failure()
        object InvalidToken : Failure()
    }

    data class Exception(val message: String?) : SignInWithTokenResponse()
}