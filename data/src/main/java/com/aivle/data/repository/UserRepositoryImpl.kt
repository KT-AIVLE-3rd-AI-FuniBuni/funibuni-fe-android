package com.aivle.data.repository

import com.aivle.data.service.UserService
import com.aivle.domain.model.user.SignInResponse
import com.aivle.domain.model.user.SignUpResponse
import com.aivle.domain.model.user.UserForSignIn
import com.aivle.domain.model.user.UserForSignUp
import com.aivle.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class UserRepositoryImpl @Inject constructor(
    private val service: UserService
) : UserRepository {

    override suspend fun signIn(userForSignIn: UserForSignIn): Flow<SignInResponse> = flow {
        when (Random.nextInt(3)) {
            0 -> emit(SignInResponse.Success)
            1 -> emit(SignInResponse.Error.NotExistsUser)
            2 -> emit(SignInResponse.Exception("Exception"))
        }
    }

    override suspend fun signUp(userForSignUp: UserForSignUp): Flow<SignUpResponse> = flow {
//        service.signUp().suspendOnSuccess {
//            emit(SignUpResponse.Success)
//        }.suspendOnError {
//            emit(SignUpResponse.Error.DuplicateID)
//        }.suspendOnException {
//            emit(SignUpResponse.Exception(message))
//        }
        when (Random.nextInt(4)) {
            0 -> emit(SignUpResponse.Success)
            1 -> emit(SignUpResponse.Error.DuplicateID)
            2 -> emit(SignUpResponse.Error.DuplicatePhoneNumber)
            3 -> emit(SignUpResponse.Exception("Exception"))
        }
    }
}