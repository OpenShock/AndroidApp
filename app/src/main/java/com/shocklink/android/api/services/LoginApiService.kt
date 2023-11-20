package com.shocklink.android.api.services

import android.database.Observable
import com.shocklink.android.api.models.LoginRequest
import com.shocklink.android.api.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("account/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}