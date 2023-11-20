package com.shocklink.android.api

import android.content.Context
import com.shocklink.android.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        if (originalRequest.url().encodedPathSegments().contains("login")) {
            return chain.proceed(originalRequest)
        }

        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("Cookie", ("openShockSession=" + TokenManager.getToken(context)) ?: "")
            .build()

        val response = chain.proceed(modifiedRequest)
        if(response.code() == 401){
            TokenManager.clearToken(context)
        }

        return response;
    }
}