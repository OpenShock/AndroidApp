package com.shocklink.android.api.models

data class MessageDataListResponse<T>(
    val message: String?,
    val data: List<T>
)
