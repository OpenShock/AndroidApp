package com.shocklink.android.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        if (originalRequest.url().encodedPathSegments().contains("login")) {
            return chain.proceed(originalRequest)
        }

        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("ShockLinkSession", token)
            .build()

        return chain.proceed(modifiedRequest)
    }
}