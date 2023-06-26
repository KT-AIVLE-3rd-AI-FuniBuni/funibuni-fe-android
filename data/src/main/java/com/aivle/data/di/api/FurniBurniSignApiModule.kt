package com.aivle.data.di.api

import com.aivle.data.api.SignApi
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
object FurniBurniSignApiModule {

    private const val TAG = "FurniBurniSignApiModule"
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val TIME_OUT = 10L
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"

    @FurniBurniSignApiProvider
    @Provides
    fun provideRetrofit(
        @FurniBurniSignApiProvider okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @FurniBurniSignApiProvider
    @Provides
    fun provideOkHttpClient(
//        @FurniBurniSignApiProvider tokenInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
//            .addInterceptor(tokenInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

//    @FurniBurniSignApiProvider
//    @Provides
//    fun provideTokenInterceptor(
//        @FurniBurniSignApiProvider repository: WebTokenRepository
//    ): Interceptor = Interceptor { chain ->
//        val refreshToken = repository.getRefreshToken()
//
//        val request = if (refreshToken == null) {
//            chain.request()
//        } else {
//            chain.request().newBuilder()
//                .addHeader(AUTHORIZATION, "$BEARER $refreshToken")
//                .build()
//        }
//
//        chain.proceed(request)
//    }

    @FurniBurniSignApiProvider
    @Provides
    fun provideSignApi(
        @FurniBurniSignApiProvider retrofit: Retrofit
    ): SignApi = retrofit.create()
}