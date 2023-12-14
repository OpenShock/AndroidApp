package com.shocklink.android.api.models.Request

data class ShareRequest(
    val permissions: Permissions,
    val limits: Limits
)
data class Permissions(
    val vibrate: Boolean,
    val sound: Boolean,
    val shock: Boolean
)

data class Limits(
    val intensity: Int,
    val duration: Int
)