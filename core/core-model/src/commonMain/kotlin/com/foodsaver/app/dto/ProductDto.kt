@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.dto

import com.foodsaver.app.model.ExpiresDateType
import com.foodsaver.app.model.ProductUnitType
import kotlinx.serialization.Serializable
import kotlin.time.Clock
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

fun Instant.castExpiresDate(): Long {
    val duration = this.minus(Clock.System.now())

    val durationDay = duration.inWholeDays
    val durationHours = if (durationDay <= 0) duration.inWholeHours
    else null

    return durationHours ?: durationDay
}

fun Instant.getExpiresType(): ExpiresDateType {
    val duration = this.minus(Clock.System.now())

    val durationDay = duration.inWholeDays
    val durationHours = if (durationDay <= 0) duration.inWholeHours
    else null

    return if (durationHours != null) ExpiresDateType.HOURS
    else ExpiresDateType.DAYS
}

fun getUnitType(unitName: String) = try {
    ProductUnitType.valueOf(unitName)
} catch (_: Exception) {
    ProductUnitType.GRAM
}

fun getCostSymbol(costUnit: String) = when (costUnit.uppercase()) {
    "USD" -> "$"
    else -> "₽"
}