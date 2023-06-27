package com.aivle.data.di.api

import com.aivle.data.api.SignWithTokenApi
import com.aivle.domain.repository.RefreshTokenRepository
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object FuniBuniSignWithTokenApiModule {

    @FuniBuniSignWithTokenApiQualifier
    @Provides
    fun provideRetrofit(
        @FuniBuniSignWithTokenApiQualifier okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @FuniBuniSignWithTokenApiQualifier
    @Provides
    fun provideOkHttpClient(
        @FuniBuniSignWithTokenApiQualifier tokenInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConstants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @FuniBuniSignWithTokenApiQualifier
    @Provides
    fun provideTokenInterceptor(
        repository: RefreshTokenRepository
    ): Interceptor = Interceptor { chain ->
        val refreshToken = repository.getRefreshToken()

        val request = if (refreshToken == null) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader(ApiConstants.AUTHORIZATION, "${ApiConstants.BEARER} $refreshToken")
                .build()
        }

        chain.proceed(request)
    }

    @FuniBuniSignWithTokenApiQualifier
    @Provides
    fun provideSignApi(
        @FuniBuniSignWithTokenApiQualifier retrofit: Retrofit
    ): SignWithTokenApi = retrofit.create()
}