package com.shocklink.android.api.services

import com.shocklink.android.api.models.LoginRequest
import com.shocklink.android.api.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("account/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}