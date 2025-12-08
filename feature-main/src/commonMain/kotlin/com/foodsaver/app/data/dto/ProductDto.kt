@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
internal data class ProductDto(
    val productId: String,
    val title: String,
    val description: String,

    val cost: Float,
    val rating: Float?,
    val organization: OrganizationDto,
    val count: Int,
    val categoryIds: List<String>,
    val photoUrl: String?,

    val expiresAt: Instant
)
