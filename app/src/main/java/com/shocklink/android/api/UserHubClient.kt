package com.shocklink.android.api

import android.content.Context
import android.util.Log
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.shocklink.android.util.TokenManager
import retrofit2.Retrofit

class UserHubClient private constructor() {
    companion object {
        private const val URL = "https://api.shocklink.net/1/hubs/user"
        fun create(context: Context): HubConnection {
            val hub = HubConnectionBuilder.create(URL).withHeader("Cookie", "openShockSession=" + TokenManager.getToken(context)?: "").build()
            hub.onClosed { exception ->
                Log.e("UserHubClient", exception.stackTraceToString())
            }
            return hub
        }
    }
}