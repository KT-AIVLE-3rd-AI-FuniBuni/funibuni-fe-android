//package com.aivle.data.api
//
//import com.aivle.data.di.api.ApiConstants
//import com.aivle.domain.repository.RefreshTokenRepository
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
//class RefreshTokenInterceptor @Inject constructor(
//    private val repository: RefreshTokenRepository
//) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val refreshToken = repository.getRefreshToken()
//
//        val request = if (refreshToken == null) {
//            chain.request()
//        } else {
//            chain.request().newBuilder()
//                .addHeader(ApiConstants.AUTHORIZATION, "${ApiConstants.BEARER} $refreshToken")
//                .build()
//        }
//
//        return chain.proceed(request)
//    }
//}