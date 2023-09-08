package com.shocklink.android.api.models

data class ShockerResponse(
    val message: String?,
    val data: List<Device>
)

data class Shocker(
    val id: String,
    val rfId: Int,
    val name: String,
    val createdOn: String,
    val isPaused: Boolean,
    val permSound: Boolean = true,
    val permVibrate: Boolean = true,
    val permShock: Boolean= true
)
