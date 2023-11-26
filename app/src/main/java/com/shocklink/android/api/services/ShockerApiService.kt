package com.shocklink.android.api.services

import com.shocklink.android.api.models.PauseRequest
import com.shocklink.android.api.models.PauseResponse
import com.shocklink.android.api.models.ShockerResponse
import com.shocklink.android.api.models.ShockerSharedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ShockerApiService {

    @GET("shockers/own")
    suspend fun ownShocker(): Response<ShockerResponse>

    @GET("shockers/shared")
    suspend fun sharedShocker(): Response<ShockerSharedResponse>

    @POST("shockers/{shockerId}/pause")
    suspend fun pauseShocker(@Path("shockerId") shockerId: String, @Body pause: PauseRequest): Response<PauseResponse>
}