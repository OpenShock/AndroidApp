package com.shocklink.android.api.services

import com.shocklink.android.api.models.LoginData
import com.shocklink.android.api.models.Request.LoginRequest
import com.shocklink.android.api.models.MessageDataListResponse
import com.shocklink.android.api.models.MessageDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("account/login")
    suspend fun login(@Body request: LoginRequest): Response<MessageDataResponse<LoginData>>
}