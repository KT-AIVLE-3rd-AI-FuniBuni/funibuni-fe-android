package com.aivle.data.di.api

import com.aivle.data.api.KakaoApi
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
object KakaoApiModule {

    private const val TAG = "KakaoApiModule"
    private const val BASE_URL = "https://dapi.kakao.com/v2/local/"
    private const val TIME_OUT = 10L
    private const val AUTHORIZATION = "Authorization"
    private const val KakaoAK = "KakaoAK"
    private const val API_KEY = "98e4ead29f1e35c1f1e494d250e4bff0"

    @KakaoApiProvider
    @Provides
    fun provideRetrofit(
        @KakaoApiProvider okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @KakaoApiProvider
    @Provides
    fun provideOkhttpClient(
        @KakaoApiProvider tokenInterceptor: Interceptor
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

    @KakaoApiProvider
    @Provides
    fun provideTokenInterceptor(): Interceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$KakaoAK $API_KEY")
            .build()

        chain.proceed(newRequest)
    }

    @KakaoApiProvider
    @Provides
    fun provideKakaoAddressApi(
        @KakaoApiProvider retrofit: Retrofit
    ): KakaoApi = retrofit.create()
}