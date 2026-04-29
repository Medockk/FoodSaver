package com.foodsaver.app.coreProductModule.data.dto

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
internal data class AddProductDto(
    val title: String,
    val description: String,

    val cost: Float,
    val costUnit: String,
    val categoryIds: List<String>,

    val count: Long = 1,
    val unit: Long,
    val unitName: String,
    val ingredients: List<String>,

    val expiresAt: Instant,
    val addedAt: Instant = Clock.System.now()
)
