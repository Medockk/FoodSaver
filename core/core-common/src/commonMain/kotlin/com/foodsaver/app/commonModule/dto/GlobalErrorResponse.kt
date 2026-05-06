package com.foodsaver.app.commonModule.dto

import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val httpCode: Int,
    val serverErrorCode: Int = 0,
    val timestamp: Long = Clock.System.now().epochSeconds,
)