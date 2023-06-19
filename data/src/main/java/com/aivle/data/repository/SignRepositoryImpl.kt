package com.aivle.data.repository

import com.aivle.data.service.SignService
import com.aivle.domain.model.sign.SignIn
import com.aivle.domain.model.sign.SignUp
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.response.SignInAutoResponse
import com.aivle.domain.response.SignInResponse
import com.aivle.domain.response.SignUpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class SignRepositoryImpl @Inject constructor(
    private val service: SignService
) : SignRepository {

    override suspend fun signIn(signIn: SignIn): Flow<SignInResponse> = flow {
//        when (Random.nextInt(3)) {
//            0 -> emit(SignInResponse.Success)
//            1 -> emit(SignInResponse.Error.NotExistsUser)
//            2 -> emit(SignInResponse.Exception("Exception"))
//        }
//        emit(SignInResponse.Success)
        emit(SignInResponse.Error.NotExistsUser)
    }

    override suspend fun signInAuto(refreshToken: String): Flow<SignInAutoResponse> = flow {
        emit(SignInAutoResponse.Success)
    }

    override suspend fun signUp(signUp: SignUp): Flow<SignUpResponse> = flow {
//        service.signUp().suspendOnSuccess {
//            emit(SignUpResponse.Success)
//        }.suspendOnError {
//            emit(SignUpResponse.Error.DuplicateID)
//        }.suspendOnException {
//            emit(SignUpResponse.Exception(message))
//        }
//        when (Random.nextInt(2)) {
//            0 -> emit(SignUpResponse.Success)
//            1 -> emit(SignUpResponse.Exception("Exception"))
//        }
        emit(SignUpResponse.Success)
    }

    override suspend fun signOut(): Flow<NothingResponse> = flow {
        emit(NothingResponse.Success)
    }

    override suspend fun withdrawal(): Flow<NothingResponse> = flow {
        emit(NothingResponse.Success)
    }
}