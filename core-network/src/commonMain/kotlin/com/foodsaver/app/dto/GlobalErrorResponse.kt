@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.dto

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val code: Int,
    val timestamp: Long = Clock.System.now().epochSeconds,
)
