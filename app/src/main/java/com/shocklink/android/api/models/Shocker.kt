package com.shocklink.android.api.models

data class Shocker(
    val id: String,
    val rfId: Int,
    val name: String,
    val createdOn: String,
    var isPaused: Boolean,
    val permSound: Boolean = true,
    val permVibrate: Boolean = true,
    val permShock: Boolean= true
)