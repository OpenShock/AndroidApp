package com.shocklink.android.api.models

data class LoginResponse(val message: String, val data: LoginData)
data class LoginData(
    val shockLinkSession: String
)