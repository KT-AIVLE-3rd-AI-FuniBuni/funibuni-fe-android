package com.aivle.domain.response

sealed class SignUpResponse {

    object Success : SignUpResponse()

//    sealed class Error : SignUpResponse() {
//        object DuplicatePhoneNumber : Error()
//    }

    data class Failure(val message: String?) : SignUpResponse()
}
