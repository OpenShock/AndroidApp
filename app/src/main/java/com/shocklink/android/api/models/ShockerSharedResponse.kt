package com.shocklink.android.api.models

data class ShockerSharedResponse(
    val message: String?,
    val data: List<User>
)

data class User(
    val id: String,
    val name: String,
    val devices: List<Device>
)

data class Device(
    val id: String,
    val name: String,
    val shockers: List<Shocker>,
    val createdOn: String
)
