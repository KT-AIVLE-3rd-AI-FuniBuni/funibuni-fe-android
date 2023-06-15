package com.aivle.data.repository

import com.aivle.data.service.UserService
import com.aivle.domain.model.user.SignUpResponse
import com.aivle.domain.model.user.UserForSignUp
import com.aivle.domain.repository.UserRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class UserRepositoryImpl @Inject constructor(
    private val service: UserService
) : UserRepository {

    override suspend fun signUp(user: UserForSignUp): Flow<SignUpResponse> = flow {
//        service.signUp().suspendOnSuccess {
//            emit(SignUpResponse.Success)
//        }.suspendOnError {
//            emit(SignUpResponse.Error.DuplicateID)
//        }.suspendOnException {
//            emit(SignUpResponse.Exception(message))
//        }
        when (Random.nextInt(3)) {
            0 -> emit(SignUpResponse.Success)
            1 -> emit(SignUpResponse.Error.DuplicateID)
            2 -> emit(SignUpResponse.Exception("Exception"))
        }
    }
}