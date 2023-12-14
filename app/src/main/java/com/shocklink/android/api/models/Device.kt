package com.shocklink.android.api.models

data class Device(
    val id: String,
    val name: String,
    val shockers: List<Shocker>,
    val createdOn: String
)