package com.shocklink.android.api.services

import com.shocklink.android.api.models.Device
import com.shocklink.android.api.models.MessageDataListResponse
import com.shocklink.android.api.models.MessageDataResponse
import com.shocklink.android.api.models.Request.PauseRequest
import com.shocklink.android.api.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ShockerApiService {

    @GET("shockers/own")
    suspend fun ownShocker(): Response<MessageDataListResponse<Device>>

    @GET("shockers/shared")
    suspend fun sharedShocker(): Response<MessageDataListResponse<User>>

    @POST("shockers/{shockerId}/pause")
    suspend fun pauseShocker(@Path("shockerId") shockerId: String, @Body pause: PauseRequest): Response<MessageDataResponse<Boolean>>
}