@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.commonModule.dto

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val httpCode: Int,
    val errorCode: Int = 0,
    val timestamp: Long = Clock.System.now().epochSeconds,
)