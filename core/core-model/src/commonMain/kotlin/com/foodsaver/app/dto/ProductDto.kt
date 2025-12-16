@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.dto

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ProductDto(
    val productId: String,

    val title: String,
    val description: String,
    val photoUrl: String?,

    val cost: Float,
    val costUnit: String,
    val oldCost: Float? = null,


    val count: Int,
    val rating: Float?,

    val organization: OrganizationDto,
    val categoryIds: List<String>,

    val unit: Long,
    val unitName: String,

    val expiresAt: Instant
)
