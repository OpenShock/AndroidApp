package com.shocklink.android.api.models

data class User(
    val id: String,
    val name: String,
    val devices: List<Device>
)