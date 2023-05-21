package com.shocklink.android.api.services

import com.shocklink.android.api.models.ShockerResponse
import com.shocklink.android.api.models.ShockerSharedResponse
import retrofit2.Response
import retrofit2.http.GET

interface ShockerApiService {

    @GET("shockers/own")
    suspend fun ownShocker(): Response<ShockerResponse>

    @GET("shockers/shared")
    suspend fun sharedShocker(): Response<ShockerSharedResponse>
}