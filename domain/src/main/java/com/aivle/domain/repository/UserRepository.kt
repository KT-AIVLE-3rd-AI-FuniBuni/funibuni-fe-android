package com.aivle.domain.repository

import com.aivle.domain.model.user.SignUpResponse
import com.aivle.domain.model.user.UserForSignUp
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signUp(user: UserForSignUp): Flow<SignUpResponse>
}