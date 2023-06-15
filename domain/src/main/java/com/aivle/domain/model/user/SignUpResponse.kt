package com.aivle.domain.model.user

sealed class SignUpResponse {

    object Success : SignUpResponse()

    sealed class Error : SignUpResponse() {
        object DuplicateID : Error()
        object DuplicatePhoneNumber : Error()
    }

    data class Exception(val message: String?) : SignUpResponse()
}
