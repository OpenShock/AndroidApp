package com.shocklink.android.api
import android.content.Context
import com.shocklink.android.api.services.LoginApiService
import com.shocklink.android.api.services.ShockerApiService
import com.shocklink.android.util.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor() {
    companion object {
        private const val BASE_URL = "https://api.shocklink.net/1/"

        private fun create(context: Context): Retrofit {
            val authInterceptor = AuthInterceptor(context)

            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getLoginApiService(context: Context): LoginApiService
        {
            return create(context).create(LoginApiService::class.java)
        }
        fun getShockerApiService(context: Context): ShockerApiService
        {
            return create(context).create(ShockerApiService::class.java)
        }
    }
}