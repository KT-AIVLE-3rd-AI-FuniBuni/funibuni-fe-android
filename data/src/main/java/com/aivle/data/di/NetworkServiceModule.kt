package com.aivle.data.di

import com.aivle.data.service.SignService
import com.aivle.data.service.UserService
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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    private const val TAG = "NetworkServiceModule"
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val TIME_OUT = 10L
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideOkHttpClient(tokenInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideTokenInterceptor(repository: WebTokenRepository): Interceptor = Interceptor { chain ->
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

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create()
    }

    @Provides
    fun provideSignService(retrofit: Retrofit): SignService {
        return retrofit.create()
    }
}