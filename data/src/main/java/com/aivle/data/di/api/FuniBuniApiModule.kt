package com.aivle.data.di.api

import com.aivle.data.api.MyBuniApi
import com.aivle.data.api.SharingPostApi
import com.aivle.data.api.SignApi
import com.aivle.data.api.UserApi
import com.aivle.data.api.WasteApi
import com.aivle.domain.repository.AccessTokenRepository
import com.aivle.domain.repository.RefreshTokenRepository
import com.loggi.core_util.extensions.logw
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object FuniBuniApiModule {

    @FuniBuniApiQualifier
    @Provides
    fun provideRetrofit(
        @FuniBuniApiQualifier okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @FuniBuniApiQualifier
    @Provides
    fun provideOkHttpClient(
        @FuniBuniApiQualifier tokenInterceptor: Interceptor,
        @FuniBuniApiQualifier authenticator: Authenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConstants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .authenticator(authenticator)
            .build()
    }

    @FuniBuniApiQualifier
    @Provides
    fun provideTokenInterceptor(
        repository: AccessTokenRepository
    ): Interceptor = Interceptor { chain ->
        val accessToken = repository.getAccessToken()

        val request = if (accessToken == null) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader(ApiConstants.AUTHORIZATION, "${ApiConstants.BEARER} $accessToken")
                .build()
        }

        chain.proceed(request)
    }

    @FuniBuniApiQualifier
    @Provides
    fun provideTokenRefreshAuthenticator(
        @FuniBuniSignApiQualifier signApi: SignApi,
        refreshTokenRepository: RefreshTokenRepository,
        accessTokenRepository: AccessTokenRepository,
    ): Authenticator = Authenticator { route, response ->
        logw("TokenRefreshAuthenticator.response=$response")

        if (response.code == 401) {
            val refreshToken = refreshTokenRepository.getRefreshToken()
                ?: return@Authenticator null

            val newAccessTokenResponse = signApi.refreshAccessToken("${ApiConstants.BEARER} $refreshToken").execute()
            val newAccessToken = newAccessTokenResponse.body()?.access_token

            val newRequest = if (newAccessTokenResponse.isSuccessful && newAccessToken != null) {
                accessTokenRepository.setAccessToken(newAccessToken)

                response.request.newBuilder()
                    .removeHeader(ApiConstants.AUTHORIZATION)
                    .addHeader(ApiConstants.AUTHORIZATION, "${ApiConstants.BEARER} $newAccessToken")
                    .build()
            } else {
                null
            }

            newRequest
        } else {
            null
        }
    }

    @FuniBuniApiQualifier
    @Provides
    fun provideUserApi(
        @FuniBuniApiQualifier retrofit: Retrofit
    ): UserApi = retrofit.create()

    @FuniBuniApiQualifier
    @Provides
    fun provideSharingPostApi(
        @FuniBuniApiQualifier retrofit: Retrofit
    ): SharingPostApi = retrofit.create()

    @FuniBuniApiQualifier
    @Provides
    fun provideWasteApi(
        @FuniBuniApiQualifier retrofit: Retrofit
    ): WasteApi = retrofit.create()

    @FuniBuniApiQualifier
    @Provides
    fun provideMyBuniApi(
        @FuniBuniApiQualifier retrofit: Retrofit
    ): MyBuniApi = retrofit.create()
}