package com.aivle.data.repository

import android.util.Log
import com.aivle.data.api.SignApi
import com.aivle.data.api.SignWithTokenApi
import com.aivle.data.datastore.AddressDatastore
import com.aivle.data.datastore.PreferencesDatastore
import com.aivle.data.di.api.FuniBuniSignApiQualifier
import com.aivle.data.di.api.FuniBuniSignWithTokenApiQualifier
import com.aivle.data.entity.token.AuthTokenEntity
import com.aivle.data.mapper.toEntity
import com.aivle.data.mapper.toModel
import com.aivle.domain.model.address.Address
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.model.sign.SignUpUser
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.response.SignInResponse
import com.aivle.domain.response.SignInWithTokenResponse
import com.aivle.domain.response.SignUpResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "SignRepositoryImpl"

class SignRepositoryImpl @Inject constructor(
    @FuniBuniSignApiQualifier
    private val signApi: SignApi,
    @FuniBuniSignWithTokenApiQualifier
    private val signApiWithToken: SignWithTokenApi,
    private val datastore: PreferencesDatastore,
    private val addressDatastore: AddressDatastore,
) : SignRepository {

    override suspend fun signIn(signInUser: SignInUser): Flow<SignInResponse> = flow {
        signApi.signIn(signInUser.toEntity())
            .suspendOnSuccess {
                saveAuthTokens(data)
                emit(SignInResponse.Success)
            }
            .suspendOnError {
                emit(SignInResponse.Error.NotFoundUser)
            }
            .suspendOnException {
                emit(SignInResponse.Exception(message))
            }
    }

    override suspend fun signInWithToken(): Flow<SignInWithTokenResponse> = flow {
        signApiWithToken.signInAuto()
            .suspendOnSuccess {
                saveAuthTokens(data)
                emit(SignInWithTokenResponse.Success)
            }
            .suspendOnError {
                emit(SignInWithTokenResponse.Failure.NotFoundUser)
                // emit(SignInWithTokenResponse.Failure.ExpiredToken)
                // emit(SignInWithTokenResponse.Failure.InvalidToken)
            }
            .suspendOnException {
                emit(SignInWithTokenResponse.Exception(message))
            }
    }

    override suspend fun signUp(signUpUser: SignUpUser): Flow<SignUpResponse> = flow {
        signApi.signUp(signUpUser.toEntity())
            .suspendOnSuccess {
                val authToken = AuthTokenEntity(data.refresh_token, data.access_token)
                saveAuthTokens(authToken)
                saveAddress(data.address.toModel())

                emit(SignUpResponse.Success)
            }
            .suspendOnFailure {
                emit(SignUpResponse.Failure(this))
            }
    }

    override suspend fun signOut(): Flow<NothingResponse> = flow {
        signApiWithToken.signOut()
            .suspendOnSuccess {
                deleteAuthTokens()
                emit(NothingResponse.Success)
            }
            .suspendOnFailure {
                emit(NothingResponse.Failure.Error(this))
            }
    }

    override suspend fun withdrawal(): Flow<NothingResponse> = flow {
        signApiWithToken.withdrawal()
            .suspendOnSuccess {
                deleteAuthTokens()
                emit(NothingResponse.Success)
            }
            .suspendOnFailure {
                emit(NothingResponse.Failure.Error(this))
            }
    }

    private suspend fun saveAuthTokens(authToken: AuthTokenEntity) {
        Log.d(TAG, "saveAuthTokens(): $authToken")
        datastore.saveAuthTokens(authToken.refresh_token!!, authToken.access_token!!)
    }

    private suspend fun deleteAuthTokens() {
        datastore.deleteAuthTokens()
    }

    private suspend fun saveAddress(address: Address) {
        addressDatastore.setAddress(address)
    }
}