package com.aivle.data.di.api

import com.aivle.data.api.SharingPostApi
import com.aivle.data.api.SignApi
import com.aivle.data.api.UserApi
import com.aivle.domain.repository.WebTokenRepository
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
object FurniBurniApiModule {

    private const val TAG = "FurniBurniApiModule"
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val TIME_OUT = 10L
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"

    @FurniBurniApiProvider
    @Provides
    fun provideRetrofit(
        @FurniBurniApiProvider okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @FurniBurniApiProvider
    @Provides
    fun provideOkHttpClient(
        @FurniBurniApiProvider tokenInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @FurniBurniApiProvider
    @Provides
    fun provideTokenInterceptor(
        @FurniBurniApiProvider repository: WebTokenRepository
    ): Interceptor = Interceptor { chain ->
        val accessToken = repository.getAccessToken()

        val request = if (accessToken == null) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader(AUTHORIZATION, "$BEARER $accessToken")
                .build()
        }

        chain.proceed(request)
    }

    @FurniBurniApiProvider
    @Provides
    fun provideUserApi(
        @FurniBurniApiProvider retrofit: Retrofit
    ): UserApi = retrofit.create()

    @FurniBurniApiProvider
    @Provides
    fun provideSharingPostApi(
        @FurniBurniApiProvider retrofit: Retrofit
    ): SharingPostApi = retrofit.create()
}