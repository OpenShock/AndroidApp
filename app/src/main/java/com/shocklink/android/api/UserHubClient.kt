package com.shocklink.android.api

import android.content.Context
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.shocklink.android.util.TokenManager
import retrofit2.Retrofit

class UserHubClient private constructor() {
    companion object {
        private const val URL = "https://api.shocklink.net/1/hubs/user"
        fun create(context: Context): HubConnection {
            val hub = HubConnectionBuilder.create(URL).withHeader("ShockLinkSession", TokenManager.getToken(context)?: "").build()
            hub.onClosed { exception ->
                // Handle connection closed event
            }
            return hub
        }
    }
}