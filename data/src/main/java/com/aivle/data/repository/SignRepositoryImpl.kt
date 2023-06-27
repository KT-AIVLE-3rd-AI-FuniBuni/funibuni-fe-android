package com.aivle.data.repository

import com.aivle.data.api.SignApi
import com.aivle.data.api.SignWithTokenApi
import com.aivle.data.di.api.FuniBuniSignApiQualifier
import com.aivle.data.di.api.FuniBuniSignWithTokenApiQualifier
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.model.sign.SignUpUser
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.response.SignInWithTokenResponse
import com.aivle.domain.response.SignInResponse
import com.aivle.domain.response.SignUpResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    @FuniBuniSignApiQualifier private val signApi: SignApi,
    @FuniBuniSignWithTokenApiQualifier private val signApiWithToken: SignWithTokenApi
) : SignRepository {

    override suspend fun signIn(signInUser: SignInUser): Flow<SignInResponse> = flow {
        signApi.signIn(signInUser)
            .suspendOnSuccess {
                emit(SignInResponse.Success)

            }
            .suspendOnError {
                emit(SignInResponse.Error.NotFoundUser)
            }
            .suspendOnException {
                emit(SignInResponse.Exception(message))
            }
        emit(SignInResponse.Error.NotFoundUser)
    }

    override suspend fun signInWithToken(): Flow<SignInWithTokenResponse> = flow {
        signApiWithToken.signInAuto()
            .suspendOnSuccess {
                emit(SignInWithTokenResponse.Success)
            }
            .suspendOnError {
                // TODO: 토큰 에러 메시지 구분하기
                emit(SignInWithTokenResponse.Failure.NotFoundUser)
                // emit(SignInWithTokenResponse.Failure.ExpiredToken)
                // emit(SignInWithTokenResponse.Failure.InvalidToken)
            }
            .suspendOnException {
                emit(SignInWithTokenResponse.Exception(message))
            }
    }

    override suspend fun signUp(signUpUser: SignUpUser): Flow<SignUpResponse> = flow {
        emit(SignUpResponse.Success)
    }

    override suspend fun signOut(): Flow<NothingResponse> = flow {
        emit(NothingResponse.Success)
    }

    override suspend fun withdrawal(): Flow<NothingResponse> = flow {
        emit(NothingResponse.Success)
    }
}