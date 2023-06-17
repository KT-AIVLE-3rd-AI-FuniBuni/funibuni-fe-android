package com.aivle.domain.repository

import com.aivle.domain.model.user.SignInResponse
import com.aivle.domain.model.user.SignUpResponse
import com.aivle.domain.model.user.UserForSignIn
import com.aivle.domain.model.user.UserForSignUp
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signIn(userForSignIn: UserForSignIn): Flow<SignInResponse>
    suspend fun signUp(userForSignUp: UserForSignUp): Flow<SignUpResponse>
}