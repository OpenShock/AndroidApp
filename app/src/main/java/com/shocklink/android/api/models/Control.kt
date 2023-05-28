package com.shocklink.android.api.models

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class Control(
    val id: String,
    val type: Int,
    @field:Min(1) @field:Max(100) val intensity: Byte,
    @field:Min(300) @field:Max(30000) val duration: UInt
)
