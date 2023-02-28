package com.example.tryanimation.try_architecture_code.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiDummyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Platform", "Android")
            .addHeader("Auth-Token", "Token-Bearer|1234567890")
            .build()
        return chain.proceed(request)
    }
}