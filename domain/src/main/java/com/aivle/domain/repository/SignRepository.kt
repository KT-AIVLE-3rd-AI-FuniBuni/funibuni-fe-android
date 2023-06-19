package com.aivle.domain.repository

import com.aivle.domain.model.sign.SignIn
import com.aivle.domain.model.sign.SignUp
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.response.SignInAutoResponse
import com.aivle.domain.response.SignInResponse
import com.aivle.domain.response.SignUpResponse
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    suspend fun signIn(signIn: SignIn): Flow<SignInResponse>

    suspend fun signInAuto(refreshToken: String): Flow<SignInAutoResponse>

    suspend fun signUp(signUp: SignUp): Flow<SignUpResponse>

    suspend fun signOut(): Flow<NothingResponse>

    suspend fun withdrawal(): Flow<NothingResponse>
}