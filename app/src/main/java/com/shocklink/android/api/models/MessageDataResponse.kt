package com.shocklink.android.api.models

data class MessageDataResponse<T>(
    val message: String?,
    val data: T
)
