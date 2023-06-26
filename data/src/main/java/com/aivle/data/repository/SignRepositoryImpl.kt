package com.aivle.data.repository

import com.aivle.data.api.SignApi
import com.aivle.data.di.api.FurniBurniSignApiProvider
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.model.sign.SignUpUser
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.response.SignInAutoResponse
import com.aivle.domain.response.SignInResponse
import com.aivle.domain.response.SignUpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    @FurniBurniSignApiProvider private val service: SignApi
) : SignRepository {

    override suspend fun signIn(signInUser: SignInUser): Flow<SignInResponse> = flow {
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

    override suspend fun signUp(signUpUser: SignUpUser): Flow<SignUpResponse> = flow {
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