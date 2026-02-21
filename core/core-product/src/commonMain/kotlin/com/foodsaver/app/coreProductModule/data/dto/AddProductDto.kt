package com.foodsaver.app.coreProductModule.data.dto

import kotlin.time.Clock
import kotlin.time.Instant

internal data class AddProductDto(
    val title: String,
    val description: String,

    val cost: Float,
    val costUnit: String,
    val categoryIds: List<String>,

    val count: Long = 1,
    val unit: Long,
    val unitName: String,

    val expiresAt: String,
    val addedAt: Instant = Clock.System.now()
)
