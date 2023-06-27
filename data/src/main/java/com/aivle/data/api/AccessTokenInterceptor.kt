//package com.aivle.data.api
//
//import com.aivle.data.di.api.ApiConstants
//import com.aivle.domain.repository.AccessTokenRepository
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
//class AccessTokenInterceptor @Inject constructor(
//    private val repository: AccessTokenRepository
//) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val accessToken = repository.getAccessToken()
//
//        val request = if (accessToken == null) {
//            chain.request()
//        } else {
//            chain.request().newBuilder()
//                .addHeader(ApiConstants.AUTHORIZATION, "${ApiConstants.BEARER} $accessToken")
//                .build()
//        }
//
//        return chain.proceed(request)
//    }
//}